package services.algorithm;

import dto.RecommendationDTO;
import exceptions.responses.Response;
import models.Behavior;
import models.Project;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.BehaviorService;
import services.ProjectService;
import services.UserService;

import java.util.List;

@Service
public class AlgorithmCore {

    private BehaviorService behaviorService;
    private ProjectService projectService;
    private UserService userService;

    @Autowired
    public AlgorithmCore(BehaviorService behaviorService, ProjectService projectService, UserService userService) {
        this.behaviorService = behaviorService;
        this.projectService = projectService;
        this.userService = userService;
    }

    public Response generateRecommendation(String apiKey, RecommendationDTO recommendationDTO) {
        Project project = projectService.getByApiKey(apiKey);
        User user = userService.findByExternalIdAndProjectOrNull(recommendationDTO.getUserId(), project);

        if (user == null) {
            // get most liked movies
        }

        List<Behavior> likedContent = behaviorService.getBehaviorsByUserAndTypeAndProject(user, true, project);

        return new Response("ok");
    }
}
