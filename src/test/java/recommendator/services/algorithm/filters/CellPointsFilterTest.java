package recommendator.services.algorithm.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;
import recommendator.services.BehaviorService;
import recommendator.services.ColumnNameService;
import recommendator.services.algorithm.AlgorithmPointsComparator;
import recommendator.services.algorithm.FiltersData;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static recommendator.services.algorithm.AlgorithmHelper.generateRowWithPointsList;
import static recommendator.services.algorithm.AlgorithmHelper.prepareRow;

@ExtendWith(MockitoExtension.class)
class CellPointsFilterTest {

    @Mock
    ColumnNameService columnNameService;
    @Mock
    BehaviorService behaviorService;
    @InjectMocks
    CellPointsFilter cellPointsFilter;

    @Test
    void filter() {

        List<Behavior> behaviors = Arrays.asList(
                new Behavior(1L, true, prepareRow("smallest", 1), null),
                new Behavior(1L, true, prepareRow("smallest", 1), null),
                new Behavior(1L, true, prepareRow("smallest", 1), null),
                new Behavior(1L, true, prepareRow("medium", 2), null),
                new Behavior(1L, true, prepareRow("biggest", 3), null)
        );

        Mockito.when(behaviorService.getBehaviorsByUser(any(User.class))).thenReturn(behaviors);
        Mockito.when(columnNameService.getCountForProject(any(Project.class))).thenReturn(1L);

        FiltersData filtersData = new FiltersData();
        filtersData.setAmount(5);
        filtersData.setUser(new User());
        filtersData.setProject(new Project());
        filtersData.setRows(generateRowWithPointsList());

        // get filtered results
        List<RowWithPoints> response = cellPointsFilter.filter(filtersData).getRows();
        // sort them by points
        response.sort(AlgorithmPointsComparator.comparator);

        assertThat(response.get(0).getCells().get(0).getValue()).isEqualTo("biggest");
        assertThat(response.get(1).getCells().get(0).getValue()).isEqualTo("smallest");
        assertThat(response.get(2).getCells().get(0).getValue()).isEqualTo("medium");
    }


}
