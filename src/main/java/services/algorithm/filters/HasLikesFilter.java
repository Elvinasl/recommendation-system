package services.algorithm.filters;

import dto.RowWithPointsDTO;
import models.entities.Project;
import models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.BehaviorService;
import services.RowService;
import services.algorithm.FiltersData;

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


        // First, we get the user and project from the filters data
        User user = filtersData.getUser();
        Project project = filtersData.getProject();

        // Secondly we get the list of rows in the most liked order
        // No amount, because the list is probably gonna be changed in next filters.
        List<RowWithPointsDTO> rows = rowService.getMostLikedContentForProjectAndUser(project, user);

        // Set the list of rows into the filters data
        filtersData.setRows(rows);

        // Return the filters data
        return filtersData;
    }



}
