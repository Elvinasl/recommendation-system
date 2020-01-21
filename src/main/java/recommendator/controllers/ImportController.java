package recommendator.controllers;

import recommendator.dto.DatasetDTO;
import recommendator.exceptions.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recommendator.models.entities.Project;
import recommendator.services.ProjectService;

@RestController
@RequestMapping("/import")
public class ImportController {

    private ProjectService projectService;

    @Autowired
    public ImportController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Handles the import request to import a given {@link DatasetDTO} to a {@link Project}
     *
     * @param apiKey The api key to find project for
     * @param data   JSON structured dataset (One of these is enough: columns and rows): <br>
     *               <pre>
     *                           {
     *               	                "columns": [
     *                                    {
     *                   	                    "name": "artist",
     *                   	                    "weight": 30
     *                                    },
     *                                ],
     *                                "rows": [
     *                                    {
     *                     	                "artist": { "value": "Elvis", "weight": 40 },
     *                                    },
     *                                ]
     *                            }
     *               </pre>
     */
    @PostMapping
    public ResponseEntity<Response> importJson(@RequestHeader("api-key") String apiKey, @RequestBody DatasetDTO data) {
        return new ResponseEntity<>(projectService.seedDatabase(apiKey, data), HttpStatus.CREATED);
    }
}
