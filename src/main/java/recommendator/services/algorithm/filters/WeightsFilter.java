package recommendator.services.algorithm.filters;

import org.springframework.stereotype.Service;
import recommendator.services.algorithm.FiltersData;

@Service
public class WeightsFilter implements AlgorithmFilter {

    @Override
    public FiltersData filter(FiltersData filtersData) {

        filtersData.getRows().forEach(row -> {
            row.getCells().forEach(cell -> {
                float weight = cell.getColumnName().getWeight() * cell.getWeight() / 20f;
                row.setPoints(row.getPoints() * weight / 100);
            });
            row.setPoints(row.getPoints() * row.getWeight());
        });

        return filtersData;
    }
}
