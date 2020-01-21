package models.containers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.entities.Cell;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellWithPoints extends Cell {

    private Float points = 0f;

    public CellWithPoints(Cell cell) {
        super(cell.getId(), cell.getValue(), cell.getColumnName(), cell.getRow(), cell.getUserPreference());
    }

}
