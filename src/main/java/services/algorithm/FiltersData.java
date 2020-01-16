package services.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Behavior;
import models.Project;
import models.Row;
import models.User;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FiltersData {
    private List<Row> rows;
    private List<Behavior> behaviors;
    private User user;
    private Project project;
    private int amount;
    private boolean finished = false;
}
