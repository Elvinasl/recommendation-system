package recommendator.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import recommendator.models.entities.Cell;
import recommendator.models.entities.Row;

import java.util.List;

@Repository
public class CellRepository extends DatabaseRepository<Cell> {

    CellRepository() {
        super(Cell.class);
    }

    /**
     * Gathers all the {@link Cell}'s from a specific row.
     *
     * @param row to where to cells should be belonging
     * @return list of all the {@link Cell}'s
     */
    @Transactional
    public List<Cell> getByRow(Row row) {
        return em.createQuery("SELECT c " +
                "FROM Cell c " +
                "WHERE c.row = :row ", Cell.class)
                .setParameter("row", row)
                .getResultList();
    }
}
