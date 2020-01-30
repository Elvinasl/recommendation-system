package recommendator.repositories;


import org.springframework.stereotype.Repository;
import recommendator.exceptions.NotFoundException;
import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.Project;
import recommendator.models.entities.Row;
import recommendator.models.entities.User;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RowRepository extends DatabaseRepository<Row> {

    RowRepository() {
        super(Row.class);
    }

    /**
     * Searches the database for entries that all have the same row_id and these rows also need to have the values
     * matching the given cells. Another requirement is that the row belongs to the given project. If all these match
     * true will be given.
     *
     * @param project the cells belong to
     * @param cellValues containing the values that should all match for a specific row
     * @return true if a row exists with the given cells
     * @throws NoResultException get's thrown when there is no row found
     */
    @Transactional
    public boolean rowExists(Project project, List<String> cellValues) throws NoResultException {
        long count = em.createQuery(
                "SELECT COUNT(cell.id) FROM Cell cell " +
                        "INNER JOIN cell.row r " +
                        "INNER JOIN r.project p " +
                        "INNER JOIN p.columnNames f " +
                        "WHERE cell.row.id IN " +
                        "(SELECT MIN(c.row.id) FROM Cell c " +
                        "WHERE c.value IN :cellValues AND c.row.project = :project " +
                        "GROUP BY c.row.id HAVING COUNT(c.id) = :size)", Long.class)
                .setParameter("cellValues", cellValues)
                .setParameter("project", project)
                .setParameter("size", (long) cellValues.size())
                .getResultList().stream().findFirst().orElseThrow(() -> new NotFoundException("Unknown row"));

            return count > 0;
    }

    /**
     * Finds a row based on the cell values and the project
     *
     * @param cellValues containing the values that should all match for a specific row
     * @param project the cells belong to
     * @return a row if the row is found
     * @throws NoResultException get's thrown when there is no row found
     */
    @Transactional
    public Row findRowByCellsAndProject(List<String> cellValues, Project project) {
        return em.createQuery(
                "SELECT cell.row FROM Cell cell " +
                        "INNER JOIN cell.row r " +
                        "INNER JOIN r.project p " +
                        "INNER JOIN p.columnNames f " +
                        "WHERE cell.row.id IN " +
                        "(SELECT MIN(c.row.id) FROM Cell c " +
                        "WHERE c.value IN :cellValues AND c.row.project = :project " +
                        "GROUP BY c.row.id HAVING COUNT(c.id) = :size)", Row.class)
                .setParameter("cellValues", cellValues)
                .setParameter("project", project)
                .setParameter("size", (long) cellValues.size())
                .getResultList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Unknown row"));

    }

    @Transactional
    public List<RowWithPoints> findMostLiked(Project project, int amount) {
        return em.createQuery("SELECT " +
                "NEW recommendator.models.containers.RowWithPoints(r, COUNT(CASE WHEN b.liked = 1 THEN 1 ELSE NULL END) - COUNT(CASE WHEN b.liked = 0 THEN 1 ELSE NULL END) ) " +
                "FROM Project p " +
                "INNER JOIN p.rows r " +
                "INNER JOIN r.cells c " +
                "INNER JOIN r.behaviors b " +
                "FETCH ALL PROPERTIES " +
                "WHERE r.project = :project " +
                "GROUP BY r.id " +
                "ORDER BY COUNT(CASE WHEN b.liked = 1 THEN 1 ELSE NULL END) - COUNT(CASE WHEN b.liked = 0 THEN 1 ELSE NULL END) DESC ", RowWithPoints.class)
                .setParameter("project", project)
                .setMaxResults(amount)
                .getResultList();
    }

    @Transactional
    public List<RowWithPoints> getMostLikedContentForProjectAndUser(Project project, User user) {
        return em.createQuery("SELECT " +
                "NEW recommendator.models.containers.RowWithPoints(r, COUNT(CASE WHEN b.liked = 1 THEN 1 ELSE NULL END) - COUNT(CASE WHEN b.liked = 0 THEN 1 ELSE NULL END) ) " +
                "FROM Row r " +
                "LEFT JOIN r.behaviors b ON b.user = :user " +
                "WHERE r.project = :project " +
                "GROUP BY r " +
                "ORDER BY COUNT(CASE WHEN b.liked = 1 THEN 1 ELSE NULL END) - COUNT(CASE WHEN b.liked = 0 THEN 1 ELSE NULL END) DESC ", RowWithPoints.class)
                .setParameter("project", project)
                .setParameter("user", user)
                .getResultList();
    }

    @Transactional
    public List<RowWithPoints> findAllByApiKey(String apiKey) {
        return em.createQuery("SELECT " +
                "NEW recommendator.models.containers.RowWithPoints(" +
                "r, " +
                "COUNT(CASE WHEN b.liked = 1 THEN 1 ELSE NULL END) - COUNT(CASE WHEN b.liked = 0 THEN 1 ELSE NULL END)) " +
                "FROM Row r " +
                "LEFT JOIN r.behaviors b " +
                "FETCH ALL PROPERTIES " +
                "WHERE r.project.apiKey = :apiKey " +
                "GROUP BY r ", RowWithPoints.class)
                .setParameter("apiKey", apiKey)
                .getResultList();
    }
}
