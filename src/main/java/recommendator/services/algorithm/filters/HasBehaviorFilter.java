package recommendator.services.algorithm.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;
import recommendator.repositories.RowRepository;
import recommendator.services.algorithm.FiltersData;

import java.util.List;

/**
 * This filter checks if FilterData has a {@link User}, if not it sets
 * the rows with the most liked rows from the {@link Project}.
 */
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
            List<RowWithPoints> rows = rowRepository.findMostLiked(project, filtersData.getAmount());
            filtersData.setRows(rows);
        }
        return filtersData;
    }
}
