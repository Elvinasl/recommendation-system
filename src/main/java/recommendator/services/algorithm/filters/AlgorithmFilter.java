package recommendator.services.algorithm.filters;

import recommendator.services.algorithm.FiltersData;

public interface AlgorithmFilter {

    FiltersData filter(FiltersData filtersData);
}
