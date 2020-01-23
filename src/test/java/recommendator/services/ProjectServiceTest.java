package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.models.entities.Project;
import recommendator.repositories.ProjectRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ColumnNameService columnNameService;

    @Mock
    private RowService rowService;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void add() {

        // Initialize project
        String apiKey = projectService.add(new Project());

        // Check the length of the key
        assertThat(apiKey.length()).isEqualTo(36);
    }

    @Test
    void getByApiKey() {

        // Initialize project
        Project project = new Project();

        // Set key
        String key = "key";

        // Set key to project
        project.setApiKey(key);

        // Mock getByApiKey which returns a project
        Mockito.when(projectService.getByApiKey(key))
                .thenReturn(project);

        // Call method with the key
        Project projectByApiKey = projectService.getByApiKey(key);

        // Check project with the given project
        assertThat(projectByApiKey).isEqualTo(project);
    }

    @Test
    void seedDatabase() {
    }

    @Test
    void seed() {
    }
}