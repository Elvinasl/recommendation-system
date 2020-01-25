package recommendator.services.algorithm.filters;

import recommendator.models.containers.RowWithPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.services.RowService;
import recommendator.services.algorithm.FiltersData;

import java.util.List;

@Service
public class HasLikesFilter implements AlgorithmFilter {

    private RowService rowService;

    @Autowired
    public HasLikesFilter(RowService rowService) {
        this.rowService = rowService;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {
        // Secondly we get the list of rows in the most liked order
        // No amount, because the list is probably gonna be changed in next filters.
        List<RowWithPoints> rows = rowService.getMostLikedContentForProjectAndUser(filtersData.getProject(), filtersData.getUser());

        // Set the list of rows into the filters containers
        filtersData.setRows(rows);

        // Return the filters containers
        return filtersData;
    }



}
