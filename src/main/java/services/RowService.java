package services;

import dto.CellDTO;
import dto.DatasetCellDTO;
import models.containers.RowWithPoints;
import exceptions.NotFoundException;
import exceptions.RowAlreadyExistsException;
import models.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ColumnNameRepository;
import repositories.RowRepository;

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
        Row newRow = new Row();
        newRow.setProject(project);

        // Converting CellDTO's to Cell objects
        List<Cell> cells = row.stream().map(cellDTO -> {
            Cell cell = new Cell();
            cell.setValue(cellDTO.getValue());
            cell.setWeight(cellDTO.getWeight());
            try {
                ColumnName columnName = columnNameRepository.getByNameAndProject(cellDTO.getColumnName(), project);
                cell.setColumnName(columnName);
            } catch (NoResultException e) {
                throw new NotFoundException("Unknown column with name: " + cellDTO.getColumnName());
            }
            cell.setRow(newRow);
            return cell;
        }).collect(Collectors.toList());

        if (rowRepository.rowExists(project, cells)) {
            throw new RowAlreadyExistsException("Row duplicate found for row: " + cells.stream().map(Cell::getValue).collect(Collectors.joining(", ")));
        } else {
            newRow.setCells(cells);
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
        List<String> cellValues = rowCells.stream().map(CellDTO::getValue).collect(Collectors.toList());
        return rowRepository.findRowByCellsAndProject(cellValues, project);
    }

    public List<RowWithPoints> getMostLikedContentForProjectAndUser(Project project, User user) {
        return rowRepository.getMostLikedContentForProjectAndUser(project, user);
    }
}
