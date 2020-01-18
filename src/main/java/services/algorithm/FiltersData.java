package services.algorithm;

import dto.RowWithPointsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Behavior;
import models.Project;
import models.User;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FiltersData {
    private List<RowWithPointsDTO> rows;
    private List<Behavior> behaviors;
    private User user;
    private Project project;
    private int amount;
    private boolean finished = false;
}
