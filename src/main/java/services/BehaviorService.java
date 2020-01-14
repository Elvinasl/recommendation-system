package services;

import dto.BehaviorDTO;
import exceptions.responses.Response;
import models.Behavior;
import models.Project;
import models.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.BehaviorRepository;

@Service
public class BehaviorService {

    private ProjectService projectService;
    private RowService rowService;
    private BehaviorRepository behaviorRepository;

    @Autowired
    public BehaviorService(ProjectService projectService, RowService rowService, BehaviorRepository behaviorRepository) {
        this.projectService = projectService;
        this.rowService = rowService;
        this.behaviorRepository = behaviorRepository;
    }

    public Response add(String apiKey, BehaviorDTO behaviorDTO) {
        Project project = projectService.getByApiKey(apiKey);

        Row row = rowService.getRowByCellDTOAndProject(behaviorDTO.getCells(), project);

        Behavior behavior = new Behavior();
        behavior.setRow(row);
        behavior.setLiked(behaviorDTO.isLiked());
        // TODO: create used logic here
//        behavior.setUser(null);
        behaviorRepository.add(behavior);
        return new Response("Behavior recorded");
    }
}
