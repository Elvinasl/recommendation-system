package controllers;

import dto.DatasetDTO;
import exceptions.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ProjectService;

@RestController
@RequestMapping("/import")
public class ImportController {

    private ProjectService projectService;

    @Autowired
    public ImportController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Handles the import request to import a given {@link DatasetDTO} to a {@link models.Project}
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
        return ResponseEntity.ok(projectService.seedDatabase(apiKey, data));
    }


}
