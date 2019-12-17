package services;

import models.Cell;
import models.Project;
import models.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.RowRepository;

import java.util.List;

@Service
public class RowService {

    private RowRepository rowRepository;

    @Autowired
    public RowService(RowRepository rowRepository) {
        this.rowRepository = rowRepository;
    }

    public void addOrUpdate(Row row, Project project) {
        row.setProject(project);
        rowRepository.add(row);
//        String colName = columnName.getName();
//        columnName.setProject(project);
//
//        if (columnNameRepository.existsByNameAndProject(colName, project)) {
//            long existingId = columnNameRepository.getByNameAndProject(colName, project).getId();
//            columnName.setId(existingId);
//            return columnNameRepository.update(columnName);
//        } else {
//            return columnNameRepository.add(columnName);
//        }

    }
}
