package recommendator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recommendator.dto.ProjectDTO;
import recommendator.dto.ReturnObjectDTO;
import recommendator.exceptions.responses.Response;
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

    @GetMapping(path = "/{api-key}")
    public ResponseEntity<ProjectDTO> get(@PathVariable("api-key") String apiKey) {
        return ResponseEntity.ok().body(projectService.getByApiKeyAndCurrentClient(apiKey));
    }

    @DeleteMapping(path = "/{api-key}")
    public ResponseEntity<Response> delete(@PathVariable("api-key") String apiKey) {
        return ResponseEntity.ok().body(projectService.deleteForCurrentClient(apiKey));
    }

    @PatchMapping(path = "/{api-key}")
    public ResponseEntity<ProjectDTO> updateName(@PathVariable("api-key") String apiKey, @RequestBody @Valid ProjectDTO projectDTO) {
        return ResponseEntity.ok().body(projectService.updateNameForCurrentClient(apiKey, projectDTO));
    }

    @PatchMapping(path = "/{api-key}/refresh-key")
    public ResponseEntity<ProjectDTO> refreshKey(@PathVariable("api-key") String apiKey) {
        return ResponseEntity.ok().body(projectService.refreshKeyForCurrentClient(apiKey));
    }

}
