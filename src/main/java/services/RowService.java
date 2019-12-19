package services;

import dto.CellDTO;
import exceptions.NotFoundException;
import models.Cell;
import models.ColumnName;
import models.Project;
import models.Row;
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
     * @param row containing all the cells to be inserted
     * @param project where the row should be inserted into
     */
    public void addOrUpdate(List<CellDTO> row, Project project) {
         // TODO: Make sure the row does not exist yet.
        Row newRow = new Row();
        newRow.setProject(project);

        List<Cell> cells = row.stream().map(cellDTO -> {
            Cell cell = new Cell();
            cell.setValue(cellDTO.getValue());
            cell.setWeight(cellDTO.getWeight());
            try {
                ColumnName columnName = columnNameRepository.getByNameAndProject(cellDTO.getColumnName(), project);
                cell.setColumnName(columnName);
            }catch(NoResultException e){
                throw new NotFoundException("Unknown column with name: " + cellDTO.getColumnName());
            }
            cell.setRow(newRow);
            return cell;
        }).collect(Collectors.toList());

        rowExists(cells, project);
        rowRepository.add(newRow);
    }


    public void rowExists(List<Cell> cells, Project project){
        long totalRows = rowRepository.rowExists(project, cells, 3);
        System.out.println(totalRows);
    }
}
