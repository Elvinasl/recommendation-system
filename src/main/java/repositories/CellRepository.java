package repositories;

import models.Cell;
import models.Row;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CellRepository extends DatabaseRepository<Cell> {

    CellRepository() {
        super(Cell.class);
    }

    @Transactional
    public List<Cell> getByRow(Row row) {
        return em.createQuery("SELECT c " +
                "FROM Cell c " +
                "WHERE c.row = :row ", Cell.class)
                .setParameter("row", row)
                .getResultList();
    }
}
