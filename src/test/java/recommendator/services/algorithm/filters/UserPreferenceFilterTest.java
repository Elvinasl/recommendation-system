package recommendator.services.algorithm.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.UserPreference;
import recommendator.services.algorithm.AlgorithmPointsComparator;
import recommendator.services.algorithm.FiltersData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static recommendator.services.algorithm.AlgorithmHelper.generateRowWithPointsList;

@ExtendWith(MockitoExtension.class)
class UserPreferenceFilterTest {

    @InjectMocks UserPreferenceFilter userPreferenceFilter;

    @Test
    void filter() {
        FiltersData filtersData = new FiltersData();

        List<RowWithPoints> rows = generateRowWithPointsList();


        rows.forEach(rowWithPoints -> rowWithPoints.getCells().forEach(cell -> {
            // setting user preferences manually based on cell values
            UserPreference userPreference = new UserPreference();
            if(cell.getValue().equals("biggest")) {
                userPreference.setWeight(100);
            }

            if(cell.getValue().equals("medium")) {
                userPreference.setWeight(70);
            }

            cell.setUserPreference(userPreference);
        }));

        filtersData.setRows(rows);

        List<RowWithPoints> response = userPreferenceFilter.filter(filtersData).getRows();
        // we need to sort them by points so we could determine which one is first one
        response.sort(AlgorithmPointsComparator.comparator);

        // we are checking if cell value is equal, userPreference weight is correct and if points are adjusted accordingly
        assertThat(response.get(0).getCells().get(0).getValue()).isEqualTo("biggest");
        assertThat(response.get(0).getCells().get(0).getUserPreference().getWeight()).isEqualTo(100);
        assertThat(response.get(0).getPoints()).isEqualTo(750);

        assertThat(response.get(1).getCells().get(0).getValue()).isEqualTo("medium");
        assertThat(response.get(1).getCells().get(0).getUserPreference().getWeight()).isEqualTo(70);
        assertThat(response.get(1).getPoints()).isEqualTo(520);

        assertThat(response.get(2).getCells().get(0).getValue()).isEqualTo("smallest");
        assertThat(response.get(2).getCells().get(0).getUserPreference().getWeight()).isEqualTo(50); // default weight is 50
        assertThat(response.get(2).getPoints()).isEqualTo(300);
    }
}
