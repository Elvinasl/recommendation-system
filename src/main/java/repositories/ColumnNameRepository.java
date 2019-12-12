package repositories;

import models.ColumnName;
import org.springframework.stereotype.Repository;

@Repository
public class ColumnNameRepository extends DatabaseRepository<ColumnName> {

    ColumnNameRepository() {
        super(ColumnName.class);
    }

}
