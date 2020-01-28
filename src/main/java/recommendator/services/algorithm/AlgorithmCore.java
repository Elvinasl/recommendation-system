package recommendator.services.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recommendator.dto.GeneratedRecommendationDTO;
import recommendator.dto.RowDTO;
import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;
import recommendator.services.ProjectService;
import recommendator.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlgorithmCore {

    private ProjectService projectService;
    private UserService userService;
    private FilterManager filterManager;

    @Value("${max_number_of_recommendations}")
    private int maxNumberOfRecommendations;

    @Autowired
    public AlgorithmCore(ProjectService projectService,
                         UserService userService,
                         FilterManager filterManager) {
        this.projectService = projectService;
        this.userService = userService;
        this.filterManager = filterManager;
    }

    @Transactional
    public GeneratedRecommendationDTO generateRecommendation(String apiKey, String externalUserId, int amount) {
        if(amount < 0){
            amount = 1;
        }else if(amount > this.maxNumberOfRecommendations){
            amount = this.maxNumberOfRecommendations;
        }

        Project project = projectService.getByApiKey(apiKey);
        User user = userService.findByExternalIdAndProjectOrNull(externalUserId, project);

        FiltersData filtersData = new FiltersData();
        filtersData.setProject(project);
        filtersData.setUser(user);
        filtersData.setAmount(amount);

        filterManager.getFilters().forEach(f -> {
            if (!filtersData.isFinished()) {
                f.filter(filtersData);
            }
        });

        // Sort list by points
        filtersData.getRows().sort(AlgorithmPointsComparator.comparator);

        // eliminating possibility to get index out of bounds
        if(filtersData.getRows().size() < amount) {
            amount = filtersData.getRows().size();
        }

        // Get only the amount that is given
        List<RowWithPoints> rows = filtersData.getRows().subList(0, amount);

        return generateDTO(rows);
    }


    private GeneratedRecommendationDTO generateDTO(List<RowWithPoints> rows) {
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
