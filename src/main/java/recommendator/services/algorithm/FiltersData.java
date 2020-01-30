package recommendator.services.algorithm;

import recommendator.models.containers.RowWithPoints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;

import java.util.List;

/**
 * This FiltersData is passed between filters. It contains all the important information required for recommending
 * and filtering.
 */
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
