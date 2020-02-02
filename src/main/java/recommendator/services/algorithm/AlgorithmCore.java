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

/**
 * The AlgorithmCore handles everything that has something to do with filtering for the recommendations.
 *
 * <b>Recommendations</b><br>
 * In order to generate recommendations there is a filter system. This filter system is a chain of filters that are
 * being executed after each other. This order is specified in the {@link FilterManager}. Every filter shares the
 * {@link FiltersData} object that keeps track of the information required for filtering.
 * Every filter gets called with that object and can modify the score for each {@link recommendator.models.entities.Row}.
 * This score defines how likely the user is going to like that specific {@link recommendator.models.entities.Row}.
 * <p>
 * At the end of filtering the list will be sorted based on the points and be converted to a {@link GeneratedRecommendationDTO}.
 */
@Service
public class AlgorithmCore {

    private ProjectService projectService;
    private UserService userService;
    private FilterManager filterManager;

    @Value("${max_number_of_recommendations}")
    private int maxNumberOfRecommendations = 10;

    @Autowired
    public AlgorithmCore(ProjectService projectService,
                         UserService userService,
                         FilterManager filterManager) {
        this.projectService = projectService;
        this.userService = userService;
        this.filterManager = filterManager;
    }

    /**
     * Creates recommendations for an user from a {@link Project} with the given api-key.
     *
     * @param apiKey         of the project to recommend for
     * @param externalUserId to recommend for
     * @param amount         limit the amount of results
     * @return All the rows that are being recommended
     */
    @Transactional
    public GeneratedRecommendationDTO generateRecommendation(String apiKey, String externalUserId, int amount) {
        if (amount < 1) {
            amount = 1;
        } else if (amount > this.maxNumberOfRecommendations) {
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
        if (filtersData.getRows().size() < amount) {
            amount = filtersData.getRows().size();
        }

        // Get only the amount that is given
        List<RowWithPoints> rows = filtersData.getRows().subList(0, amount);

        return generateDTO(rows);
    }

    /**
     * Converts a list of {@link RowWithPoints} to {@link GeneratedRecommendationDTO}. This because the filter
     * does work with a list of {@link RowWithPoints} but the client should receive a DTO.
     *
     * @param rows to convert
     * @return DTO containing all the rows
     */
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
