package recommendator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import recommendator.repositories.ColumnNameRepository;

@Service
public class ColumnNameService {

    private ColumnNameRepository columnNameRepository;

    @Autowired
    public ColumnNameService(ColumnNameRepository columnNameRepository) {
        this.columnNameRepository = columnNameRepository;
    }

    /**
     * Add or updates the given {@link ColumnName} into the database.
     *
     * @param columnName    The columnName that should be added or updated
     * @param project       The project where the columnName belongs to
     * @return the added or updated {@link ColumnName}
     */
    ColumnName addOrUpdate(ColumnName columnName, Project project) {
        // Get the columnName name
        String colName = columnName.getName();

        // Set project for the columnName
        columnName.setProject(project);

        // Check if the columnName exists
        if (columnNameRepository.existsByNameAndProject(colName, project)) {

            // Get the existing id from the columnName
            long existingId = columnNameRepository.getByNameAndProject(colName, project).getId();

            // Set the id from the database
            columnName.setId(existingId);

            // Update the columnName and return it
            return columnNameRepository.update(columnName);
        } else {

            // Add the columnName and return it
            return columnNameRepository.add(columnName);
        }
    }

    /**
     * Get the number of column(Name)s which the project has
     *
     * @param project   The project to count for
     * @return the number of column(Name)s
     */
    public long getCountForProject(Project project) {
        return columnNameRepository.getCountForProject(project);
    }
}
