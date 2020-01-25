package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.dto.DatasetDTO;
import recommendator.dto.DatasetRowDTO;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import recommendator.repositories.ProjectRepository;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        String apiKey = projectService.add(new Project());
        // Check the length of the key
        assertThat(apiKey.length()).isEqualTo(36);
    }

    @Test
    void getByApiKey() {

        Project project = new Project();
        String key = "key";
        project.setApiKey(key);

        // Mock getByApiKey which returns a project
        Mockito.when(projectRepository.getByApiKey(key))
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

        String key = "key";
        DatasetDTO datasetDTO = new DatasetDTO();
        Project project = new Project();

        // Not the mocked version must be called
        Mockito.when(mockedProjectService.seedDatabase(key, datasetDTO)).thenCallRealMethod();

        // Mock getByApiKey
        Mockito.when(mockedProjectService.getByApiKey(key)).thenReturn(project);

        // Call the method to test
        Response response = mockedProjectService.seedDatabase("key", datasetDTO);

        // Verify that seed is called with the given parameters
        verify(mockedProjectService, times(1)).seed(datasetDTO, project);

        // Check response
        assertThat(response.getMessage()).isEqualTo("Data has been added");
    }

    @Test
    void seed() {

        DatasetDTO datasetDTO = new DatasetDTO();
        Project project = new Project();

        // Call the seed method
        projectService.seed(datasetDTO, project);

        // There are no columns, so this method shouldn't be called
        verify(columnNameService, times(0)).addOrUpdate(new ColumnName(), project);

        // There are no rows, so this method shouldn't be called
        verify(rowService, times(0)).addOrUpdate(null, project);

        // Set two columns in the dataset
        datasetDTO.setColumns(Arrays.asList(new ColumnName(), new ColumnName()));

        // Call the seed method
        projectService.seed(datasetDTO, project);

        // There are two columns, so the method should be called one time for both
        verify(columnNameService, times(2)).addOrUpdate(new ColumnName(), project);

        // There are no rows, so this method shouldn't be called
        verify(rowService, times(0)).addOrUpdate(null, project);

        // Set two rows in the dataset
        datasetDTO.setRows(Arrays.asList(new DatasetRowDTO(), new DatasetRowDTO()));

        // Call the seed method
        projectService.seed(datasetDTO, project);

        // the seed method is called for the second time with two
        // columns so this method is called four times
        verify(columnNameService, times(4)).addOrUpdate(new ColumnName(), project);

        // There are two rows, so the method should be called twice
        verify(rowService, times(2)).addOrUpdate(null, project);
    }

}
