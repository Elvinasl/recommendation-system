package controllers;

import models.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import services.ProjectService;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final ProjectService projectService;

    @Autowired
    public ImportController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Handles the import request to import a given {@link Dataset} to a {@link models.Project}
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void importJson(@RequestHeader("api-key") String apiKey, @RequestBody Dataset data) {
        // TODO: Ask about custom exceptions etc.
        projectService.seedDatabase(apiKey, data);
    }


}
