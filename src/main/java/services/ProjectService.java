package services;

import dto.Dataset;
import exceptions.responses.Response;
import models.Cell;
import models.ColumnName;
import models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ProjectRepository;

import java.util.UUID;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private ColumnNameService columnNameService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ColumnNameService columnNameService) {
        this.projectRepository = projectRepository;
        this.columnNameService = columnNameService;
    }

    // returns api key
    public String add(Project project) {
        String apiKey = UUID.randomUUID().toString();
        project.setApiKey(apiKey);
        projectRepository.add(project);
        return apiKey;
    }

    /**
     * Gets the project belonging to the given api key
     *
     * @param key The api key to find project for
     * @return {@link Project}
     */
    public Project getByApiKey(String key) {
        return projectRepository.getByApiKey(key);
    }

    /**
     * Seeds the database with the given dataset
     *
     * @param apiKey used to get the project
     * @param data   containing project information
     */
    public Response seedDatabase(String apiKey, Dataset data) {

        // We first need to get the project by the given api key
        Project project = this.getByApiKey(apiKey);

        // Then we seed the project with the given dataset
        this.seed(data, project);

        return new Response("Created");
    }

//
//     *               <pre>
//     *                           {
//     *               	                "columns": [
//     *                                    {
//     *                   	                    "name": "artist",
//     *                   	                    "weight": 30
//                    *                      },
//     *                                ],
//     *                                "rows": [
//     *                                    {
//     *                     	                "artist": { "value": "Elvis", "weight": 40 },
//     *                                    },
//     *                                ]
//     *                            }
//     *               </pre>

    /**
     * Seed the project with data from the dataset.
     *
     * @param dataset This data comes from the client
     */
    public void seed(Dataset dataset, Project project) {

        dataset.getColumns().forEach(columnName -> columnNameService.addOrUpdate(columnName, project));

    }
}
