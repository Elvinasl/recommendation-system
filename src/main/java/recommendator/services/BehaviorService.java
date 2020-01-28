package recommendator.services;

import recommendator.dto.BehaviorDTO;
import recommendator.exceptions.NotFoundException;
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

    /**
     * This method creates a behavior (liken or disliked) for the content.
     * If the row is not existing, it also creates a row. If it exists, just references it (foreign key)
     * If the user if sending a feedback (behavior) for the first time, we also save his id.
     * It also creates (or adjusts if exists) user preferences, which later can be used for more accurate recommendation
     * @param apiKey api key of the project
     * @param behaviorDTO
     * @return general response
     */
    public Response add(String apiKey, BehaviorDTO behaviorDTO) {
        Project project = projectService.getByApiKey(apiKey);

        // if we don't have a row in our db, lets create a new one!
        Row row;
        try {
            row = rowService.getRowByCellDTOAndProject(behaviorDTO.getCells(), project);
        } catch (NotFoundException e) {
            row = rowService.create(behaviorDTO.getCells(), project);
        }

        String externalUserId = behaviorDTO.getUserId();

        User user = userService.findByExternalIdAndProjectOrNull(externalUserId, project);

        // this is a new user, we never saw them in this project. Lets save him
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
