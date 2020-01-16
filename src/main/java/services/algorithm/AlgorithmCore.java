package services.algorithm;

import dto.GeneratedRecommendationDTO;
import dto.RecommendationDTO;
import dto.RowDTO;
import models.Behavior;
import models.Project;
import models.Row;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.RowRepository;
import services.BehaviorService;
import services.CellService;
import services.ProjectService;
import services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlgorithmCore {

    private BehaviorService behaviorService;
    private ProjectService projectService;
    private UserService userService;
    private RowRepository rowRepository;
    private CellService cellService;

    private RecommendationDTO recommendationDTO;

    @Autowired
    public AlgorithmCore(BehaviorService behaviorService, ProjectService projectService, UserService userService, RowRepository rowRepository, CellService cellService) {
        this.behaviorService = behaviorService;
        this.projectService = projectService;
        this.userService = userService;
        this.rowRepository = rowRepository;
        this.cellService = cellService;
    }

    public GeneratedRecommendationDTO generateRecommendation(String apiKey, RecommendationDTO recommendationDTO) {
        GeneratedRecommendationDTO generatedRecommendationDTO = new GeneratedRecommendationDTO();
        this.recommendationDTO = recommendationDTO;

        Project project = projectService.getByApiKey(apiKey);
        User user = userService.findByExternalIdAndProjectOrNull(recommendationDTO.getUserId(), project);

        if (user == null) {
            return getMostLikedContent(project, generatedRecommendationDTO);
        }

        List<Behavior> likedContent = behaviorService.getBehaviorsByUserAndTypeAndProject(user, true, project);

        return generatedRecommendationDTO;
    }

    private GeneratedRecommendationDTO getMostLikedContent(Project project, GeneratedRecommendationDTO generatedRecommendationDTO) {
        // get most liked movies
        List<Row> rows = rowRepository.findMostLiked(project, recommendationDTO.getAmount());
        generatedRecommendationDTO.setRows(rows.stream()
                .map(row -> {
                    RowDTO rowDTO = new RowDTO();
                    rowDTO.convertCellsToDTO(cellService.getByRow(row));
                    return rowDTO;
                }).collect(Collectors.toList()));

        return generatedRecommendationDTO;
    }
}
