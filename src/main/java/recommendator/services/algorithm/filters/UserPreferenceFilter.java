package recommendator.services.algorithm.filters;

import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.services.algorithm.FiltersData;
import recommendator.services.UserPreferenceService;

/**
 * This filter takes user preference depending on a row cell and adjusts row points based on that
 */
@Service
public class UserPreferenceFilter implements AlgorithmFilter {

    @Override
    public FiltersData filter(FiltersData filtersData) {

        // we are looping through every row
        for (RowWithPoints row : filtersData.getRows()) {

            float userPrefPoints = 0F;
            // grabbing row cells and summing up all their weight (if they have one)
            for (Cell cell : row.getCells()) {
                if (cell.getUserPreference() != null) {
                    // parsing int to float
                    userPrefPoints += (float) cell.getUserPreference().getWeight();
                }
            }

            // adding the points from row and previos filters
            userPrefPoints += row.getWeight() + row.getPoints() * 20f;
            row.setPoints(userPrefPoints);
        }

        return filtersData;
    }
}
