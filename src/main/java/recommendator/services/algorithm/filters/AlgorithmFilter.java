package recommendator.services.algorithm.filters;

import recommendator.services.algorithm.FiltersData;

/**
 * This interface defines the way each filter for the algorithm should look like.
 */
public interface AlgorithmFilter {

    /**
     * This method gets called whenever the filter is ran by the algorithm.
     * @param filtersData current filtered data by previous filters
     * @return modified FiltersData
     */
    FiltersData filter(FiltersData filtersData);
}
