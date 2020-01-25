package recommendator.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import recommendator.dto.ProjectDTO;
import recommendator.models.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProjectDTO> add(@RequestBody @Valid ProjectDTO project) {
        return ResponseEntity.ok().body(projectService.add(project));
    }
}
