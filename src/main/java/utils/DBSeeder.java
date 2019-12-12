package utils;

import models.Dataset;
import models.Project;
import org.springframework.stereotype.Component;
import services.ProjectService;

@Component
public class DBSeeder {


    private final ProjectService projectService;

    public DBSeeder(ProjectService projectService) {
        this.projectService = projectService;
    }


    public void dataset(Project project, Dataset data) {


    }
}
