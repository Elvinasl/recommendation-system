package services;

import dto.BehaviorDTO;
import exceptions.responses.Response;
import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.BehaviorRepository;

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

        userPreferenceService.createOrAdjust(project, user, row);

        return new Response("Behavior recorded");
    }

    public List<Behavior> getBehaviorsByUserAndTypeAndProject(User user, boolean liked, Project project) {
        return behaviorRepository.getBehaviorsByUserAndTypeAndProject(user, liked, project);
    }
}
