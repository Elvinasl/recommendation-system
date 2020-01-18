package services.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterManager {

    private List<AlgorithmFilter> filters;

    @Autowired
    public FilterManager(HasBehaviorFilter hasBehaviorFilter,
                         HasLikesFilter hasLikesFilter,
                         WeightsFilter weightsFilter) {
        this.filters = new ArrayList<>();

        this.filters.add(hasBehaviorFilter);
        this.filters.add(hasLikesFilter);
        this.filters.add(weightsFilter);
    }

    public List<AlgorithmFilter> getFilters() {
        return this.filters;
    }


}
