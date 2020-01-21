package services.algorithm;

import dto.GeneratedRecommendationDTO;
import dto.RecommendationDTO;
import dto.RowDTO;
import dto.RowWithPointsDTO;
import models.entities.Project;
import models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import services.CellService;
import services.ProjectService;
import services.UserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlgorithmCore {

    private ProjectService projectService;
    private UserService userService;
    private CellService cellService;
    private FilterManager filterManager;

    @Autowired
    public AlgorithmCore(ProjectService projectService,
                         UserService userService,
                         CellService cellService,
                         FilterManager filterManager) {
        this.projectService = projectService;
        this.userService = userService;
        this.cellService = cellService;
        this.filterManager = filterManager;
    }

    @Transactional
    public GeneratedRecommendationDTO generateRecommendation(String apiKey, RecommendationDTO recommendationDTO) {
        Project project = projectService.getByApiKey(apiKey);
        User user = userService.findByExternalIdAndProjectOrNull(recommendationDTO.getUserId(), project);

        FiltersData filtersData = new FiltersData();
        filtersData.setProject(project);
        filtersData.setUser(user);
        filtersData.setAmount(recommendationDTO.getAmount());

        filterManager.getFilters().forEach(f -> {
            if (!filtersData.isFinished()) {
                f.filter(filtersData);
            }
        });

        // Sort list by points
        Comparator<RowWithPointsDTO> compareRowsByPoints = Comparator.comparing(RowWithPointsDTO::getPoints).reversed();
        Collections.sort(filtersData.getRows(), compareRowsByPoints);

        // Get only the amount that is given
        List<RowWithPointsDTO> rows = filtersData.getRows().subList(0, recommendationDTO.getAmount());


        return generateDTO(rows);
    }


    private GeneratedRecommendationDTO generateDTO(List<RowWithPointsDTO> rows) {
        GeneratedRecommendationDTO generatedRecommendationDTO = new GeneratedRecommendationDTO();
        generatedRecommendationDTO.setRows(rows.stream()
                .map(row -> {
                    RowDTO rowDTO = new RowDTO();
                    rowDTO.convertCellsToDTO(row.getCells());
                    return rowDTO;
                }).collect(Collectors.toList()));

        return generatedRecommendationDTO;
    }

}
