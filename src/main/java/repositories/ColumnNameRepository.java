package repositories;

import exceptions.NotFoundException;
import models.ColumnName;
import models.Project;
import org.springframework.stereotype.Repository;

@Repository
public class ColumnNameRepository extends DatabaseRepository<ColumnName> {

    ColumnNameRepository() {
        super(ColumnName.class);
    }

    public ColumnName getByNameAndProject(String name, Project project) {

        ColumnName columnName = (ColumnName) em.createQuery(
                "SELECT c FROM ColumnName c WHERE c.name = :cName AND c.project = :cProject")
                .setParameter("cName", name)
                .setParameter("cProject", project)
                .getSingleResult();

        if (columnName == null) {
            throw new NotFoundException("Column name not found");
        }
        return columnName;
    }
}
