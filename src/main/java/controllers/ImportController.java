package controllers;

import models.Dataset;
import models.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import services.ProjectService;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final ProjectService projectService;

    public ImportController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Dataset importJson(@RequestHeader("api-key") String apiKey, @RequestBody Dataset data) {
        Project project = projectService.getByApiKey(apiKey);
        data.seedProject(project);
        projectService.update(project);
        return data;
    }



}
