package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Behavior;
import models.Cell;
import models.Row;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowWithPointsDTO extends Row {

    private Float points;

    public RowWithPointsDTO(Object row, Object points) {
        Row r = (Row) row;
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
