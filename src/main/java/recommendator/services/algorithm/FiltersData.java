package recommendator.services.algorithm;

import recommendator.models.containers.RowWithPoints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FiltersData {
    private List<RowWithPoints> rows;
    private List<Behavior> behaviors;
    private User user;
    private Project project;
    private int amount;
    private boolean finished = false;
}
