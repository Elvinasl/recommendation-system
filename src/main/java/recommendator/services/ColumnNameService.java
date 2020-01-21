package recommendator.services;

import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.repositories.ColumnNameRepository;

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
