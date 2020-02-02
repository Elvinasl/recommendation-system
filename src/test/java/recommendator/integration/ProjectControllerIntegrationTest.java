package recommendator.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.ProjectDTO;
import recommendator.exceptions.NotFoundException;
import recommendator.models.entities.Project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ProjectControllerIntegrationTest extends IntegrationTest {

    @BeforeEach
    void createClientAndLogin() throws Exception {
        clientService.add(client);
        login();
    }

    @Test
    public void createProject() throws Exception {
        ProjectDTO project = new ProjectDTO("testProject");
        // Creating a new project and validating if a api-key is handed back
        MockHttpServletResponse projectRequest = postRequest(project, "/projects");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThat(projectRequest.getContentAsString()).contains("name").contains("apiKey");

        // Creating a project without being authorized
        logout();
        projectRequest = postRequest(project, "/projects");
        assertThat(projectRequest.getStatus()).isEqualTo(403);
    }

    @Test
    public void list() throws Exception {
        addProjectToDatabase("p1", "1");
        addProjectToDatabase("p2", "2");

        // Calling /projects and check if all the client's projects are being returned
        MockHttpServletResponse projectRequest = getRequest("/projects");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThat(projectRequest.getContentAsString()).contains("{\"objects\":[{\"name\":\"p1\",\"apiKey\":\"1\"},{\"name\":\"p2\",\"apiKey\":\"2\"}]}");

        // Creating a project without being authorized
        httpHeaders.remove("Authorization");
        projectRequest = getRequest("/projects");
        assertThat(projectRequest.getStatus()).isEqualTo(403);
    }

    @Test
    public void get() throws Exception {
        addProjectToDatabase("p1", "1");
        addProjectToDatabase("p2", "2");

        // Creating a new project and validating if a api-key is handed back
        MockHttpServletResponse projectRequest = getRequest("/projects/1");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThat(projectRequest.getContentAsString()).contains("{\"name\":\"p1\",\"apiKey\":\"1\"}");
    }

    @Test
    public void delete() throws Exception {
        addProjectToDatabase("p1", "1");
        addProjectToDatabase("p2", "2");

        // Delete project with api-key 1
        MockHttpServletResponse projectRequest = deleteRequest("/projects/1");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThrows(NotFoundException.class, () -> projectRepository.getByApiKey("1"));
        // Checking if project 2 still exists
        assertThat(projectRepository.getByApiKey("2")).isNotNull();

        // Check if the user is not allowed to delete the project when unauthenticated
        logout();
        projectRequest = deleteRequest("/projects/1");
        assertThat(projectRequest.getStatus()).isEqualTo(403);
    }

    @Test
    public void update() throws Exception {
        addProjectToDatabase("p1", "1");
        addProjectToDatabase("p2", "2");

        // Update the project name
        MockHttpServletResponse projectRequest = patchRequest(new ProjectDTO("updated"), "/projects/1");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThat(projectRepository.getByApiKey("1").getName()).isEqualTo("updated");
        // Making sure that project 2 is not changed
        assertThat(projectRepository.getByApiKey("2").getName()).isEqualTo("p2");
        // Check if the user is not allowed to update the project name when unauthenticated
        logout();
        projectRequest = patchRequest(new ProjectDTO("updated"), "/projects/1");
        assertThat(projectRequest.getStatus()).isEqualTo(403);
    }

    @Test
    public void refreshKey() throws Exception {
        addProjectToDatabase("p1", "1");
        addProjectToDatabase("p2", "2");

        // Refreshing the api-key
        MockHttpServletResponse projectRequest = patchRequest(new ProjectDTO("updated"), "/projects/1/refresh-key");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThat(projectRequest.getContentAsString()).contains("name").contains("apiKey");

        // Check if the user is not allowed to refresh the key when unauthenticated
        logout();
        projectRequest = patchRequest(new ProjectDTO("updated"), "/projects/1/refresh-key");
        assertThat(projectRequest.getStatus()).isEqualTo(403);
    }

    private void addProjectToDatabase(String name, String apiKey) {
        Project project = new Project();
        project.setName(name);
        project.setApiKey(apiKey);
        project.setClient(clientRepository.getByEmail(client.getEmail()));
        projectRepository.add(project);
    }
}
