package repositories;

import dto.CellDTO;
import models.Cell;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CellRepository extends DatabaseRepository<Cell> {

    CellRepository() {
        super(Cell.class);
    }

    public List<Cell> getCells(List<CellDTO> cells) {
        List<Cell> cellsFromDB = new ArrayList<>();

        for (CellDTO cell : cells) {
            List<Cell> cellsPartFromDB = em.createQuery("SELECT c FROM Cell c WHERE c.columnName.name = :columnName AND c.value = :value ")
                    .setParameter("columnName", cell.getColumnName())
                    .setParameter("value", cell.getValue())
                    .getResultList();
            cellsFromDB.addAll(cellsFromDB);
        }

        return cellsFromDB;
    }
}
