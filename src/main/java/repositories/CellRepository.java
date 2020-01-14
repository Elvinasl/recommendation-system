package repositories;

import dto.CellDTO;
import models.Cell;
import models.Project;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CellRepository extends DatabaseRepository<Cell> {

    CellRepository() {
        super(Cell.class);
    }

    public List<Cell> getCells(List<CellDTO> cells, Project project) {
        List<Cell> cellsFromDB = new ArrayList<>();

        for (CellDTO cell : cells) {
            List<Cell> cellResults = em.createQuery("SELECT c " +
                    "FROM Cell c " +
                    "WHERE c.columnName.name = :columnName " +
                    "AND c.value = :celValue " +
                    "AND c.row.project = :project")
                    .setParameter("columnName", cell.getColumnName())
                    .setParameter("celValue", cell.getValue())
                    .setParameter("project", project)
                    .getResultList();
            cellsFromDB.addAll(cellResults);
        }
        return cellsFromDB;
    }
}
