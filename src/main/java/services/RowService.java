package services;

import dto.CellDTO;
import dto.DatasetCellDTO;
import exceptions.NotFoundException;
import exceptions.RowAlreadyExistsException;
import models.Cell;
import models.ColumnName;
import models.Project;
import models.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ColumnNameRepository;
import repositories.RowRepository;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RowService {

    private RowRepository rowRepository;
    private ColumnNameRepository columnNameRepository;
    private CellService cellService;

    @Autowired
    public RowService(RowRepository rowRepository, ColumnNameRepository columnNameRepository, CellService cellService) {
        this.rowRepository = rowRepository;
        this.columnNameRepository = columnNameRepository;
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

    public Row getRowByCellDTOAndProject(List<CellDTO> rowCells, Project project) {

        List<Cell> cells = cellService.getCellsFromDB(rowCells);

//        List<Long> possibleRows = new ArrayList<>();
        HashMap<Long, Integer> possibleRows = new HashMap<>();

        for (Cell cell : cells) {
            Integer times = possibleRows.get(cell.getRow().getId());
            if (times == null) {
                times = 0;
            }
            possibleRows.put(cell.getRow().getId(), times + 1);
        }

        Integer max = 0;
        Long rowId = 0L;
        for (Map.Entry<Long, Integer> entry : possibleRows.entrySet()) {
            if (entry.getValue() > max) {
                rowId = entry.getKey();
            }
        }

        return rowRepository.getById(rowId);


//        List<Cell> cells = row.stream()
//                .map(cellDTO -> {
//                    Cell cell = new Cell();
//                    cell.setValue(cellDTO.getValue());
//                    ColumnName columnName = columnNameRepository.getByNameAndProject(cellDTO.getColumnName(), project);
//                    cell.setColumnName(columnName);
//                    return cell;
//                })
//                .collect(Collectors.toList());

//        return getByCellsAndProject(cells, project);
    }

//    private Row getByCellsAndProject(List<Cell> cells, Project project) {
//
////        cells = rowRepository.findByCells(cells, project);
//
//
//
////        return null;
////        return rowRepository.findByCellsAndProject(cells, project);
//    }
}
