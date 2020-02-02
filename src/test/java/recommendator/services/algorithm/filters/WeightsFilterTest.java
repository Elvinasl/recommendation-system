package recommendator.services.algorithm.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.models.containers.RowWithPoints;
import recommendator.services.algorithm.AlgorithmPointsComparator;
import recommendator.services.algorithm.FiltersData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static recommendator.services.algorithm.AlgorithmHelper.generateRowWithPointsList;

@ExtendWith(MockitoExtension.class)
class WeightsFilterTest {

    @InjectMocks
    WeightsFilter weightsFilter;

    @Test
    void filter() {
        FiltersData filtersData = new FiltersData();
        filtersData.setRows(generateRowWithPointsList());

        List<RowWithPoints> response = weightsFilter.filter(filtersData).getRows();

        // we need to sort them by points so we could determine which one is first one
        response.sort(AlgorithmPointsComparator.comparator);

        assertThat(response.get(0).getCells().get(0).getValue()).isEqualTo("biggest");
        assertThat(response.get(1).getCells().get(0).getValue()).isEqualTo("medium");
        assertThat(response.get(2).getCells().get(0).getValue()).isEqualTo("smallest");
    }
}
