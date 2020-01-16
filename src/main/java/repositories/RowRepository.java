package repositories;


import dto.CellDTO;
import models.Cell;
import models.Project;
import models.Row;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param cells   containing the values that should be inserted into the database
     * @return true if a row exists with the given cells
     * @throws NoResultException get's thrown when there is no row found
     */
    @Transactional
    public boolean rowExists(Project project, List<Cell> cells) throws NoResultException {
        long count = (long) em.createQuery(
                "SELECT COUNT(cell.id) FROM Cell cell " +
                        "INNER JOIN cell.row r " +
                        "INNER JOIN r.project p " +
                        "INNER JOIN p.columnNames f " +
                        "WHERE cell.row.id IN " +
                        "(SELECT MIN(c.row.id) FROM Cell c " +
                        "WHERE c.value IN :cellValues AND c.row.project = :project " +
                        "GROUP BY c.row.id HAVING COUNT(c.id) = SIZE(f))")
                .setParameter("cellValues", cells.stream().map(Cell::getValue).collect(Collectors.toList()))
                .setParameter("project", project)
                .getSingleResult();

        return count > 0;
    }

    /**
     * Finds a row based on the cell values and the project
     *
     * @param project the cells belong to
     * @param cells containing the values that should all match for a specific row
     * @return a row if the row is found
     * @throws NoResultException get's thrown when there is no row found
     */
    @Transactional
    public Row findRowByCellsAndProject(List<CellDTO> cells, Project project) throws NoResultException {
        return (Row) em.createQuery(
                "SELECT cell.row FROM Cell cell " +
                        "INNER JOIN cell.row r " +
                        "INNER JOIN r.project p " +
                        "INNER JOIN p.columnNames f " +
                        "WHERE cell.row.id IN " +
                        "(SELECT MIN(c.row.id) FROM Cell c " +
                        "WHERE c.value IN :cellValues AND c.row.project = :project " +
                        "GROUP BY c.row.id HAVING COUNT(c.id) = SIZE(f))")
                .setParameter("cellValues", cells.stream().map(CellDTO::getValue).collect(Collectors.toList()))
                .setParameter("project", project)
                .getSingleResult();
    }

    @Transactional
    public List<Row> findMostLiked(Project project, int amount) {

        return em.createQuery("SELECT r " +
                "FROM Project p " +
                "INNER JOIN p.rows r " +
                "INNER JOIN r.behaviors b " +
                "WHERE r.project = :project " +
                "GROUP BY r.id " +
                "ORDER BY count(CASE WHEN b.liked = 1 THEN 1 ELSE NULL END) - COUNT(CASE WHEN b.liked = 0 THEN 1 ELSE NULL END) DESC ", Row.class)
                .setParameter("project", project)
                .setMaxResults(amount)
                .getResultList();
    }
}
