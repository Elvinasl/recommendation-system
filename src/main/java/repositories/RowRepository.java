package repositories;


import models.Cell;
import models.ColumnName;
import models.Project;
import models.Row;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RowRepository extends DatabaseRepository<Row> {

    RowRepository() {
        super(Row.class);
    }

    @Transactional
    public long rowExists(Project project, List<Cell> cells, long totalColumnNames) throws NoResultException {
//        SELECT `Cell`.*
//                FROM `Cell`
//        WHERE `row_id` IN (SELECT MIN(`row_id`)
//                FROM `Cell`
//        WHERE (`value`) IN ("TestValue3","TestValue4")
//        GROUP BY `row_id`
//        HAVING COUNT(*)=:totalColumnNamesForProject)
        return (Long) em.createQuery(
                "SELECT COUNT(cell.id) FROM Cell cell WHERE cell.row.id in (SELECT MIN(c.id) FROM Cell c WHERE c.value IN :cellValues GROUP BY c.row.id HAVING COUNT(c)=:totalColumns)")
                .setParameter("cellValues", cells.stream().map(Cell::getValue).collect(Collectors.toList()))
                .setParameter("totalColumns", totalColumnNames)
                .getSingleResult();
    }
}
