package recommendator.services.algorithm.filters;

import org.springframework.stereotype.Service;
import recommendator.services.algorithm.FiltersData;

@Service
/**
 *  Sums upp cell, columnName (column) and row weights. After that it also takes row points into account.
**/
public class WeightsFilter implements AlgorithmFilter {

    @Override
    public FiltersData filter(FiltersData filtersData) {

        filtersData.getRows().forEach(row -> {
            row.getCells().forEach(cell -> {
                // multiplies column weight with cell weight (totalColWeight)
                float totalColWeight = cell.getColumnName().getWeight() * cell.getWeight() / 20f;
                // multiplies row points with totalColWeight
                row.setPoints(row.getPoints() * totalColWeight / 100);
            });
            // multiplies row points with row weight
            row.setPoints(row.getPoints() * row.getWeight());
        });

        return filtersData;
    }
}
