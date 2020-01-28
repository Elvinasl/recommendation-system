package recommendator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.dto.CellDTO;
import recommendator.dto.DatasetCellDTO;
import recommendator.exceptions.NotFoundException;
import recommendator.exceptions.RowAlreadyExistsException;
import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.*;
import recommendator.repositories.ColumnNameRepository;
import recommendator.repositories.RowRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RowService {

    private RowRepository rowRepository;
    private ColumnNameRepository columnNameRepository;

    @Autowired
    public RowService(RowRepository rowRepository, ColumnNameRepository columnNameRepository) {
        this.rowRepository = rowRepository;
        this.columnNameRepository = columnNameRepository;
    }

    /**
     * Inserts the given {@link CellDTO} into the database.
     *
     * @param row     containing all the cells to be inserted
     * @param project where the row should be inserted into
     */
    public void addOrUpdate(List<DatasetCellDTO> row, Project project) {
        // TODO: Update weight if row already exists

        // Initialize a new row
        Row newRow = new Row();

        // Set the project to the row
        newRow.setProject(project);

        // Converting CellDTOs to Cell objects
        List<Cell> cells = row.stream().map(cellDTO -> {
            // Instantiate new Cell object
            Cell cell = new Cell();

            // Set cell value and weight
            cell.setValue(cellDTO.getValue());
            cell.setWeight(cellDTO.getWeight());

            try {
                // Try to get the columnName for the by the columnName of cellDTO and the project
                ColumnName columnName = columnNameRepository.getByNameAndProject(cellDTO.getColumnName(), project);

                // Set the columnName which comes from the repository
                cell.setColumnName(columnName);
            } catch (NoResultException e) {
                // Column couldn't be found, so give our own exception
                throw new NotFoundException("Unknown column with name: " + cellDTO.getColumnName());
            }

            // Add the new row to the cell
            cell.setRow(newRow);

            // Return the cell
            return cell;
        }).collect(Collectors.toList());

        List<String> cellValues = cells.stream().map(Cell::getValue).collect(Collectors.toList());
        // Check if a row already exists with those cells
        if (rowRepository.rowExists(project, cellValues)) {
            // Throw an exception when the row exists
            throw new RowAlreadyExistsException("Row duplicate found for row: " + cells.stream().map(Cell::getValue).collect(Collectors.joining(", ")));
        } else {

            // Set cells to the row
            newRow.setCells(cells);

            // Add the row to the database
            rowRepository.add(newRow);
        }
    }

    /**
     * Returns a row if there is any row found with the given cell values and the same project
     *
     * @param rowCells all the cell values that make a row
     * @param project  that the row belongs to
     * @return a row if there is any match with all the cell values and project
     */
    public Row getRowByCellDTOAndProject(List<CellDTO> rowCells, Project project) {

        // Get the cell values as list<String> to find the row for
        List<String> cellValues = rowCells.stream().map(CellDTO::getValue).collect(Collectors.toList());

        // Get the most liked content from the repository
        return rowRepository.findRowByCellsAndProject(cellValues, project);
    }

    public List<RowWithPoints> getMostLikedContentForProjectAndUser(Project project, User user) {

        // Get the most liked content from the repository
        return rowRepository.getMostLikedContentForProjectAndUser(project, user);
    }
}
