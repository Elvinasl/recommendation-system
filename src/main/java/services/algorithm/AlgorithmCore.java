package services.algorithm;

import dto.GeneratedRecommendationDTO;
import dto.RecommendationDTO;
import dto.RowDTO;
import models.Project;
import models.Row;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.CellService;
import services.ProjectService;
import services.UserService;

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
        return generateDTO(filtersData.getRows());
    }



    private GeneratedRecommendationDTO generateDTO(List<Row> rows) {
        GeneratedRecommendationDTO generatedRecommendationDTO = new GeneratedRecommendationDTO();
        generatedRecommendationDTO.setRows(rows.stream()
                .map(row -> {
                    RowDTO rowDTO = new RowDTO();
                    rowDTO.convertCellsToDTO(cellService.getByRow(row));
                    return rowDTO;
                }).collect(Collectors.toList()));

        return generatedRecommendationDTO;
    }

}
