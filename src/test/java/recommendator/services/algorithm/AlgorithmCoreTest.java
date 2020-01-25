package recommendator.services.algorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.dto.GeneratedRecommendationDTO;
import recommendator.dto.RecommendationDTO;
import recommendator.models.entities.Project;
import recommendator.repositories.RowRepository;
import recommendator.services.ProjectService;
import recommendator.services.UserService;
import recommendator.services.algorithm.filters.AlgorithmFilter;
import recommendator.services.algorithm.filters.HasBehaviorFilter;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static recommendator.services.algorithm.AlgorithmHelper.generateRowWithPointsList;
import static recommendator.services.algorithm.AlgorithmHelper.prepareRow;

@ExtendWith(MockitoExtension.class)
class AlgorithmCoreTest {

    @Mock ProjectService projectService;
    @Mock UserService userService;
    @Mock FilterManager filterManager;
    @InjectMocks AlgorithmCore algorithmCore;
    @Mock RowRepository rowRepository;
    @InjectMocks HasBehaviorFilter hasBehaviorFilter;

    @Test
    void generateRecommendation() {

        List<AlgorithmFilter> filters = new ArrayList<>();
        filters.add(hasBehaviorFilter);
        // we are checking if second filter is not called if first one finishes
        filters.add(hasBehaviorFilter);

        RecommendationDTO recommendationDTO = new RecommendationDTO("1", 5);



        Mockito.when(projectService.getByApiKey(anyString())).thenReturn(new Project());
        // we are returning null, to trigger hasBehaviorFilter
        Mockito.when(userService.findByExternalIdAndProjectOrNull(anyString(), any(Project.class))).thenReturn(null);
        Mockito.when(filterManager.getFilters()).thenReturn(filters);
        Mockito.when(rowRepository.findMostLiked(any(Project.class), anyInt())).thenReturn(generateRowWithPointsList());

        GeneratedRecommendationDTO response = algorithmCore.generateRecommendation("key", recommendationDTO);

        // verifying that filter was called once
        verify(rowRepository, times(1)).findMostLiked(any(Project.class), anyInt());

        assertThat(response.getRows().get(0).getCells().get(0).getValue()).isEqualTo("biggest");
        assertThat(response.getRows().get(1).getCells().get(0).getValue()).isEqualTo("medium");
        assertThat(response.getRows().get(2).getCells().get(0).getValue()).isEqualTo("smallest");
    }


}
