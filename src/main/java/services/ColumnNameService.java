package services;

import models.ColumnName;
import models.Project;
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

    public void addOrUpdate(ColumnName columnName, Project project) {
        ColumnName existingColName = columnNameRepository.getByNameAndProject(columnName.getName(), project);

        if (existingColName == null) {
            columnName.setProject(project);
            columnNameRepository.add(columnName);
        } else {
            existingColName.setWeight(columnName.getWeight());
            columnNameRepository.add(columnName);
        }
    }
}
