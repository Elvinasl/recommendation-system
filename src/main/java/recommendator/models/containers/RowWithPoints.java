package recommendator.models.containers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Cell;
import recommendator.models.entities.Row;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowWithPoints extends Row {

    private Float points;

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
