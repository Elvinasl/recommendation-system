package recommendator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recommendator.dto.CellDTO;
import recommendator.dto.DatasetCellDTO;
import recommendator.dto.ReturnObjectDTO;
import recommendator.dto.RowDTO;
import recommendator.exceptions.NotFoundException;
import recommendator.exceptions.RowAlreadyExistsException;
import recommendator.exceptions.responses.Response;
import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.*;
import recommendator.repositories.RowRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This service contains all the logic for everything that has something to do with Rows.
 */
@Service
public class RowService {

    private RowRepository rowRepository;
    private ColumnNameService columnNameService;
    private CellService cellService;

    @Autowired
    public RowService(RowRepository rowRepository, ColumnNameService columnNameService, CellService cellService) {
        this.rowRepository = rowRepository;
        this.columnNameService = columnNameService;
        this.cellService = cellService;
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
                ColumnName columnName = columnNameService.getByNameAndProject(cellDTO.getColumnName(), project);

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

    /**
     * Returns a list of rows which belongs to the project based on the api key.
     * It also provides points which are number of likes (like +1 or dislike -1) for each row.
     *
     * @param apiKey Api key of the project
     * @return List of rows belonging to that project
     */
    @Transactional
    public ReturnObjectDTO getByApiKey(String apiKey) {
        List<RowWithPoints> rows = rowRepository.findAllByApiKey(apiKey);

        List<RowDTO> rowDTOs = rows.stream()
                .map(row -> {
                    RowDTO rowDTO = new RowDTO();
                    rowDTO.setId(row.getId());
                    rowDTO.convertCellsToDTO(row.getCells());
                    rowDTO.setReactions(Math.round(row.getPoints()));
                    return rowDTO;
                })
                .collect(Collectors.toList());

        return new ReturnObjectDTO<>(rowDTOs);
    }

    /**
     * Deletes a row by the given row id
     *
     * @param rowId Id of the row
     * @return global response class
     */
    public Response deleteRow(long rowId) {
        rowRepository.remove(rowId);
        return new Response("Row deleted!");
    }

    /**
     * Updates row: updates cell values based on rowDTO
     *
     * @param rowId  Id of the row to be updated
     * @param rowDTO rowDto with cell values to be updated
     * @return Global response with a message
     */
    public Response updateRow(long rowId, RowDTO rowDTO) {
        Row row = rowRepository.getById(rowId);
        if (row == null) {
            throw new NotFoundException("Row with this id was not found!");
        }

        List<Cell> cells = cellService.updateCellValues(rowDTO.getCells());
        row.setCells(cells);

        return new Response("Row updated!");
    }

    /**
     * Creates a row
     *
     * @param cellDTOs
     * @param project
     * @return saved row
     */
    public Row create(List<CellDTO> cellDTOs, Project project) {
        Row row = new Row();
        row.setProject(project);
        Row persistedRow = rowRepository.add(row);

        List<Cell> cells = cellDTOs.stream()
                .map(cellDTO -> cellService.create(cellDTO, project, persistedRow))
                .collect(Collectors.toList());

        row.setCells(cells);
        return rowRepository.update(persistedRow);
    }
}
