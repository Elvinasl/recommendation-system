package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.dto.DatasetDTO;
import recommendator.exceptions.responses.Response;
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

        // Mock the service, because seedDatabase has a call to
        // a method in projectService which is tested separately
        ProjectService mockedProjectService = Mockito.mock(projectService.getClass());

        // Initialize key
        String key = "key";

        // Initialize datasetDTO
        DatasetDTO datasetDTO = new DatasetDTO();

        // Initialize project
        Project project = new Project();

        // Not the mocked version must be called
        Mockito.when(mockedProjectService.seedDatabase(key, datasetDTO)).thenCallRealMethod();

        // Call the method to test
        Response response = mockedProjectService.seedDatabase("key", datasetDTO);

        // Check response
        assertThat(response.getMessage()).isEqualTo("Data has been addeds");
    }

    @Test
    void seed() {
    }

}