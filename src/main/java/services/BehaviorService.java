package services;

import dto.BehaviorDTO;
import exceptions.responses.Response;
import models.Project;
import org.springframework.stereotype.Service;

@Service
public class BehaviorService {

    private ProjectService projectService;


    public Response add(String apiKey, BehaviorDTO behaviorDTO) {
        Project project = projectService.getByApiKey(apiKey);

        return new Response("Behavior recorded");
    }
}
