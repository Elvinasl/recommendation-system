package services.algorithm;

import dto.RowWithPointsDTO;
import models.Cell;
import models.Project;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.UserPreferenceService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserPreferenceFilter implements AlgorithmFilter {

    private UserPreferenceService userPreferenceService;

    @Autowired
    public UserPreferenceFilter(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {

        // we are looping through every row
        for (RowWithPointsDTO row : filtersData.getRows()) {

            float userPrefPoints = 0F;
            // grabbing row cells and summing up all their weight (if they have one)
            for (Cell cell : row.getCells()) {
                if (cell.getUserPreference() != null) {
                    // parsing int to float
                    userPrefPoints += (float) cell.getUserPreference().getWeight();
                }
            }

            // adding the points from row and previos filters
            userPrefPoints += row.getWeight() + row.getPoints();
            row.setPoints(userPrefPoints);
        }

        return filtersData;
    }
}
