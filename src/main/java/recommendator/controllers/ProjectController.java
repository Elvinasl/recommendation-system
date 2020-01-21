package recommendator.controllers;

import recommendator.models.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recommendator.services.ProjectService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/project")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody @Valid Project project) {
        return ResponseEntity.ok(projectService.add(project));
    }
}
