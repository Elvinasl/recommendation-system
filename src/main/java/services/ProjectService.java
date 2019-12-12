package services;

import models.Project;
import org.springframework.stereotype.Service;
import repositories.ProjectRepository;

import java.util.List;

@Service
public class ProjectService implements DatabaseServiceInterface<Project> {

    private final ProjectRepository projectRepository;

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

    public Project getByApiKey(String key) {
        return projectRepository.getByApiKey(key);
    }

}
