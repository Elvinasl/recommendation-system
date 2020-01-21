package recommendator.services.algorithm.filters;

import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.services.RowService;
import recommendator.services.algorithm.FiltersData;
import recommendator.services.BehaviorService;

import java.util.List;

@Service
public class HasLikesFilter implements AlgorithmFilter {

    private RowService rowService;
    private BehaviorService behaviorService;

    @Autowired
    public HasLikesFilter(RowService rowService, BehaviorService behaviorService) {
        this.rowService = rowService;
        this.behaviorService = behaviorService;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {


        // First, we get the user and project from the filters containers
        User user = filtersData.getUser();
        Project project = filtersData.getProject();

        // Secondly we get the list of rows in the most liked order
        // No amount, because the list is probably gonna be changed in next filters.
        List<RowWithPoints> rows = rowService.getMostLikedContentForProjectAndUser(project, user);

        // Set the list of rows into the filters containers
        filtersData.setRows(rows);

        // Return the filters containers
        return filtersData;
    }



}
