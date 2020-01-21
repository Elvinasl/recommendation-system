package recommendator.services;

import recommendator.dto.BehaviorDTO;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Project;
import recommendator.models.entities.Row;
import recommendator.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.repositories.BehaviorRepository;

import java.util.List;

@Service
public class BehaviorService {

    private ProjectService projectService;
    private RowService rowService;
    private BehaviorRepository behaviorRepository;
    private UserService userService;
    private UserPreferenceService userPreferenceService;

    @Autowired
    public BehaviorService(ProjectService projectService, RowService rowService, BehaviorRepository behaviorRepository, UserService userService, UserPreferenceService userPreferenceService) {
        this.projectService = projectService;
        this.rowService = rowService;
        this.behaviorRepository = behaviorRepository;
        this.userService = userService;
        this.userPreferenceService = userPreferenceService;
    }

    public Response add(String apiKey, BehaviorDTO behaviorDTO) {
        Project project = projectService.getByApiKey(apiKey);

        Row row = rowService.getRowByCellDTOAndProject(behaviorDTO.getCells(), project);

        String externalUserId = behaviorDTO.getUserId();

        User user = userService.findByExternalIdAndProjectOrNull(externalUserId, project);

        if (user == null) {
            user = new User();
            user.setProject(project);
            user.setExternalUserId(externalUserId);
            userService.add(user);
        }

        Behavior behavior = new Behavior();
        behavior.setRow(row);
        behavior.setLiked(behaviorDTO.isLiked());
        behavior.setUser(user);
        behaviorRepository.add(behavior);

        userPreferenceService.createOrAdjust(user, row, behavior);

        return new Response("Behavior recorded");
    }

    public List<Behavior> getBehaviorsByUserAndTypeAndProject(User user, boolean liked, Project project) {
        return behaviorRepository.getBehaviorsByUserAndTypeAndProject(user, liked, project);
    }

    public List<Behavior> getBehaviorsByUser(User user) {
        return behaviorRepository.getBehaviorsByUser(user);
    }
}
