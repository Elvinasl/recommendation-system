package services;

import models.Dataset;
import models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ProjectRepository;

import java.util.List;

@Service
public class ProjectService implements DatabaseServiceInterface<Project> {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void add(Project o) {
        projectRepository.add(o);
    }

    @Override
    public void update(Project o) {
        projectRepository.update(o);
    }

    @Override
    public List<Project> list() {
        return projectRepository.list();
    }

    @Override
    public Project getById(int id) {
        return projectRepository.getById(id);
    }

    @Override
    public void remove(int id) {
        projectRepository.remove(id);
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
    public void seedDatabase(String apiKey, Dataset data) {

        // We first need to get the project by the given api key
        Project project = this.getByApiKey(apiKey);

        // Then we seed the project with the given dataset
        project.seed(data);

        // Update project with all their newly created columns/cells/rows
        this.update(project);
    }

}
