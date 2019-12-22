package repositories;

import models.Cell;
import org.springframework.stereotype.Repository;

@Repository
public class CellRepository extends DatabaseRepository<Cell> {

    CellRepository() {
        super(Cell.class);
    }

}
