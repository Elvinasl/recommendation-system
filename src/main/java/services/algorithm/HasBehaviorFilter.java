package services.algorithm;

import dto.RowWithPointsDTO;
import models.Project;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.RowRepository;

import java.util.List;

@Service
public class HasBehaviorFilter implements AlgorithmFilter {

    private final RowRepository rowRepository;

    @Autowired
    public HasBehaviorFilter(RowRepository rowRepository) {
        this.rowRepository = rowRepository;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {

        User user = filtersData.getUser();
        Project project = filtersData.getProject();

        if (user == null) {
            filtersData.setFinished(true);
            List<RowWithPointsDTO> rows = rowRepository.findMostLiked(project, filtersData.getAmount());
            filtersData.setRows(rows);
        }
        return filtersData;
    }
}
