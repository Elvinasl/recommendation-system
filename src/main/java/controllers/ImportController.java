package controllers;

import models.Dataset;
import models.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public Dataset importJson(@RequestHeader("api-key") String apiKey, @RequestBody Dataset data) {
        // TODO: Handling exceptions
        Project project = projectService.getByApiKey(apiKey);

        data.seedProject(project);
        projectService.update(project);
        return data;
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void importXml(@RequestBody String data) {

    }


}
