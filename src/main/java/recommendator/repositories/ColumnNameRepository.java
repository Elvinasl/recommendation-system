package recommendator.repositories;

import org.springframework.stereotype.Repository;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Repository
public class ColumnNameRepository extends DatabaseRepository<ColumnName> {

    ColumnNameRepository() {
        super(ColumnName.class);
    }

    /**
     * Gathers a {@link ColumnName} from the database that has a specific name and belongs to a specific {@link Project}.
     *
     * @param name    of the column
     * @param project the {@link ColumnName} must belong to
     * @return The found {@link ColumnName}
     * @throws NoResultException when there is no result
     */
    @Transactional
    public ColumnName getByNameAndProject(String name, Project project) throws NoResultException {
        return (ColumnName) em.createQuery(
                "SELECT c FROM ColumnName c WHERE c.name = :cName AND c.project = :cProject")
                .setParameter("cName", name)
                .setParameter("cProject", project)
                .getSingleResult();
    }

    /**
     * Checks if the {@link ColumnName} exists for a specific {@link Project}.
     *
     * @param name    of the column
     * @param project the {@link ColumnName} should belong to
     * @return true if the {@link ColumnName} exists
     */
    @Transactional
    public boolean existsByNameAndProject(String name, Project project) {
        long count = (long) em.createQuery(
                "SELECT count(c) FROM ColumnName c WHERE c.name = :cName AND c.project = :cProject")
                .setParameter("cName", name)
                .setParameter("cProject", project)
                .getSingleResult();

        return count > 0;
    }


    /**
     * Counts the total {@link ColumnName} that a specific {@link Project} has.
     *
     * @param project to count for
     * @return Sum of total {@link ColumnName}'s
     */
    @Transactional
    public long getCountForProject(Project project) {
        return (long) em.createQuery(
                "SELECT count(c) FROM ColumnName c WHERE c.project = :project")
                .setParameter("project", project)
                .getSingleResult();
    }
}
