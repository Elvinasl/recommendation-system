package services;

import dto.Dataset;
import exceptions.responses.Response;
import models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ProjectRepository;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
        project.seed(data);

        // Update project with all their newly created columns/cells/rows
        projectRepository.update(project);

        return new Response("Created");
    }

}
