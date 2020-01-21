package services;

import models.entities.ColumnName;
import models.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ColumnNameRepository;

@Service
public class ColumnNameService {

    private ColumnNameRepository columnNameRepository;

    @Autowired
    public ColumnNameService(ColumnNameRepository columnNameRepository) {
        this.columnNameRepository = columnNameRepository;
    }

    ColumnName addOrUpdate(ColumnName columnName, Project project) {
        String colName = columnName.getName();
        columnName.setProject(project);

        if (columnNameRepository.existsByNameAndProject(colName, project)) {
            long existingId = columnNameRepository.getByNameAndProject(colName, project).getId();
            columnName.setId(existingId);
            return columnNameRepository.update(columnName);
        } else {
            return columnNameRepository.add(columnName);
        }
    }

    public long getCountForProject(Project project) {
        return columnNameRepository.getCountForProject(project);
    }
}
