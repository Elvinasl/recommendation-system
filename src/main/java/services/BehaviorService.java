package services;

import dto.BehaviorDTO;
import exceptions.responses.Response;
import models.Project;
import models.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BehaviorService {

    private ProjectService projectService;
    private RowService rowService;

    @Autowired
    public BehaviorService(ProjectService projectService, RowService rowService) {
        this.projectService = projectService;
        this.rowService = rowService;
    }

    public Response add(String apiKey, BehaviorDTO behaviorDTO) {
        Project project = projectService.getByApiKey(apiKey);

        Row row = rowService.getRowByCellDTOAndProject(behaviorDTO.getCells(), project);

        return new Response("Behavior recorded");
    }
}
