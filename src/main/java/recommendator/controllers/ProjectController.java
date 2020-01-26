package recommendator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recommendator.dto.ProjectDTO;
import recommendator.dto.ReturnObjectDTO;
import recommendator.services.ProjectService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/projects")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ReturnObjectDTO<ProjectDTO>> list() {
        return ResponseEntity.ok().body(projectService.listByCurrentClient());
    }
    @PostMapping
    public ResponseEntity<ProjectDTO> add(@RequestBody @Valid ProjectDTO project) {
        return ResponseEntity.ok().body(projectService.add(project));
    }
}
