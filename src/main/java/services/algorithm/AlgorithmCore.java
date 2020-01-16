package services.algorithm;

import dto.RecommendationDTO;
import models.Behavior;
import models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.BehaviorService;
import services.ProjectService;

import java.util.List;

@Service
public class AlgorithmCore {

    private BehaviorService behaviorService;
    private ProjectService projectService;

    @Autowired
    public AlgorithmCore(BehaviorService behaviorService, ProjectService projectService) {
        this.behaviorService = behaviorService;
        this.projectService = projectService;
    }

    public List<Behavior> generateRecommendation(String apiKey, RecommendationDTO recommendationDTO) {
        Project project = projectService.getByApiKey(apiKey);
        return null;
    }
}
