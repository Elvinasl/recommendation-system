package services.algorithm.filters;

import models.containers.RowWithPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.BehaviorService;
import services.algorithm.FiltersData;

import java.util.Collections;
import java.util.Comparator;

@Service
public class WeightsFilter implements AlgorithmFilter {

    private BehaviorService behaviorService;

    @Autowired
    public WeightsFilter(BehaviorService behaviorService) {
        this.behaviorService = behaviorService;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {

        filtersData.getRows().forEach(row -> {
            row.getCells().forEach(cell -> {
                float weight = cell.getColumnName().getWeight() * cell.getWeight() / 20f;
                row.setPoints(row.getPoints() * weight / 100);
            });
            row.setPoints(row.getPoints() * row.getWeight());
        });

        // Sort list by points
        Comparator<RowWithPoints> compareByPoints = Comparator.comparing(RowWithPoints::getPoints).reversed();
        Collections.sort(filtersData.getRows(), compareByPoints);

        return filtersData;
    }
}
