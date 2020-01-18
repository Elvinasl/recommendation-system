package services.algorithm;

import models.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightsFilter implements AlgorithmFilter {

    @Autowired
    public WeightsFilter() {

    }

    @Override
    public FiltersData filter(FiltersData filtersData) {
        List<Row> rows = filtersData.getRows();

        System.out.println(rows.size());


        return filtersData;
    }
}
