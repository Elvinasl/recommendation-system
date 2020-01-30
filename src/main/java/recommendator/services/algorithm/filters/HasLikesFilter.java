package recommendator.services.algorithm.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.models.containers.RowWithPoints;
import recommendator.services.RowService;
import recommendator.services.algorithm.FiltersData;

import java.util.List;

/**
 * This filter sets the {@link FiltersData} rows with the most liked content
 * for a specific {@link recommendator.models.entities.User} and {@link recommendator.models.entities.Project}
 */
@Service
public class HasLikesFilter implements AlgorithmFilter {

    private RowService rowService;

    @Autowired
    public HasLikesFilter(RowService rowService) {
        this.rowService = rowService;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {
        // We get the list of rows in the most liked order
        // No amount, because the list is probably gonna be changed in next filters.
        List<RowWithPoints> rows = rowService.getMostLikedContentForProjectAndUser(filtersData.getProject(), filtersData.getUser());

        rows.forEach(rowWithPoints -> {

            // When a row is liked or disliked, subtract 2 points so it wil not be
            // on top of the recommendations because the user has already seen it
            if(rowWithPoints.getBehaviors() != null && rowWithPoints.getBehaviors().size() > 0){
                rowWithPoints.setPoints(rowWithPoints.getPoints()-2);
            }
        });

        // Set the list of rows into the filters containers
        filtersData.setRows(rows);

        // Return the filters containers
        return filtersData;
    }
}
