package services;

import dto.CellDTO;
import models.Cell;
import models.ColumnName;
import models.Project;
import models.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ColumnNameRepository;
import repositories.RowRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RowService {

    private RowRepository rowRepository;
    private final ColumnNameRepository columnNameRepository;

    @Autowired
    public RowService(RowRepository rowRepository, ColumnNameRepository columnNameRepository) {
        this.rowRepository = rowRepository;
        this.columnNameRepository = columnNameRepository;
    }

    /**
     * Inserts the given {@link CellDTO} into the database.
     * @param row containing all the cells to be inserted
     * @param project where the row should be inserted into
     */
    public void addOrUpdate(List<CellDTO> row, Project project) {
         // TODO: Make sure the row does not exist yet.
        List<Cell> cells = row.stream().map(cellDTO -> {
            Cell cell = new Cell();
            cell.setValue(cellDTO.getValue());
            cell.setWeight(cellDTO.getWeight());
            ColumnName columnName = columnNameRepository.getByNameAndProject(cellDTO.getColumnName(), project);
            cell.setColumnName(columnName);
            return cell;
        }).collect(Collectors.toList());

        Row newRow = new Row();
        newRow.setProject(project);
        newRow.setCells(cells);
        rowRepository.add(newRow);
    }
}
