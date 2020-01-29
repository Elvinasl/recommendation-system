package recommendator.services.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.services.algorithm.filters.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This FilterManager keeps track of each filter and defines the order of which they are executed.
 */
@Service
public class FilterManager {

    private List<AlgorithmFilter> filters;

    @Autowired
    public FilterManager(HasBehaviorFilter hasBehaviorFilter,
                         HasLikesFilter hasLikesFilter,
                         WeightsFilter weightsFilter,
                         UserPreferenceFilter userPreferenceFilter,
                         CellPointsFilter cellPointsFilter
    ) {
        this.filters = new ArrayList<>();

        this.filters.add(hasBehaviorFilter);
        this.filters.add(hasLikesFilter);
        this.filters.add(weightsFilter);
        this.filters.add(userPreferenceFilter);
        this.filters.add(cellPointsFilter);
    }

    /**
     * @return list of all filters in the right order.
     */
    public List<AlgorithmFilter> getFilters() {
        return this.filters;
    }


}
