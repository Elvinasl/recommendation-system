package repositories;

import models.ColumnName;
import models.Project;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Repository
public class ColumnNameRepository extends DatabaseRepository<ColumnName> {

    ColumnNameRepository() {
        super(ColumnName.class);
    }

    @Transactional
    public ColumnName getByNameAndProject(String name, Project project) throws NoResultException {
        return (ColumnName) em.createQuery(
                "SELECT c FROM ColumnName c WHERE c.name = :cName AND c.project = :cProject")
                .setParameter("cName", name)
                .setParameter("cProject", project)
                .getSingleResult();
    }

    @Transactional
    public boolean existsByNameAndProject(String name, Project project) {

        long count = (long) em.createQuery(
                "SELECT count(c) FROM ColumnName c WHERE c.name = :cName AND c.project = :cProject")
                .setParameter("cName", name)
                .setParameter("cProject", project)
                .getSingleResult();

        return count > 0;
    }
}
