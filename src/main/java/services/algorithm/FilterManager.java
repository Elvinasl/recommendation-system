package services.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterManager {

    private AlgorithmFilter likeFilter;

    private AlgorithmFilter currentFilter;

    @Autowired
    public FilterManager(AlgorithmFilter likeFilter) {
        this.likeFilter = likeFilter;
    }

    public List<AlgorithmFilter> getFilters() {
        List<AlgorithmFilter> filters = new ArrayList<>();
        filters.add(likeFilter);

        return filters;
    }

    public boolean hasNext(){
        // ....
    }

    public AlgorithmFilter nextFilter(){
        // ...
    }

}
