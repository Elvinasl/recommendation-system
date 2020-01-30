package recommendator.models.containers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Cell;
import recommendator.models.entities.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * This model is an extension to the regular {@link Row} It adds points to a cell that can be used
 * int he algorithm to score a {@link Row}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowWithPoints extends Row {

    private Float points;

    /**
     * Initializes the row and sets the points for this row.
     * <b>Info</b><br>
     * <i>Since this method is called from a jbql query the points need to be of type {@link Object}
     * to prevent conversion exceptions.</i>
     * @param r row to use
     * @param points containing the points
     */
    public RowWithPoints(Row r, Object points) {
        this.setId(r.getId());
        this.setProject(r.getProject());
        this.setWeight(r.getWeight());

        List<Behavior> behaviors = r.getBehaviors();
        if (behaviors == null) behaviors = new ArrayList<>();
        this.setBehaviors(behaviors);

        List<Cell> cells = r.getCells();
        if (cells == null) cells = new ArrayList<>();
        this.setCells(cells);

        this.points = Float.parseFloat(String.valueOf(points));
    }

}
