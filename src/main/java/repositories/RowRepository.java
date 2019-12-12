package repositories;


import models.Row;
import org.springframework.stereotype.Repository;

@Repository
public class RowRepository extends DatabaseRepository<Row> {

    RowRepository() {
        super(Row.class);
    }

}
