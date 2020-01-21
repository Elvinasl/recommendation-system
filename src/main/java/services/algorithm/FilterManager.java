package services.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.algorithm.filters.*;

import java.util.ArrayList;
import java.util.List;

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

    public List<AlgorithmFilter> getFilters() {
        return this.filters;
    }


}
