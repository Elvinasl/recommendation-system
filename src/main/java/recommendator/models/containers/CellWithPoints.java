package recommendator.models.containers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.Cell;

/**
 * This model is an extension to the regular {@link Cell} It adds points to a cell that can be used
 * int he algorithm to score a {@link Cell}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellWithPoints extends Cell {

    private Float points = 0f;

    public CellWithPoints(Cell cell) {
        super(cell.getId(), cell.getValue(), cell.getColumnName(), cell.getRow(), cell.getUserPreference());
    }

}
