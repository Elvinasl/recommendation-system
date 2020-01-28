package recommendator.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.ProjectDTO;
import recommendator.repositories.CellRepository;
import recommendator.repositories.ClientRepository;
import recommendator.repositories.ProjectRepository;
import recommendator.repositories.RowRepository;

import static org.assertj.core.api.Assertions.assertThat;



public class ProjectControllerIntegrationTest extends IntegrationTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void createClientAndLogin() throws Exception {
        clientService.add(client);
        login();
    }

    @AfterEach
    void cleanupClient(){
        projectRepository.deleteAll();
        clientRepository.deleteAll();
        logout();
    }

    @Test
    public void createProject() throws Exception {
        ProjectDTO project = new ProjectDTO("testProject");
        // Creating a new project and validating if a api-key is handed back
        MockHttpServletResponse projectRequest = postRequest(project, "/projects");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThat(projectRequest.getContentAsString()).contains("message").contains("name");


        // Creating a project without being authorized
        httpHeaders.remove("Authorization");
        projectRequest = postRequest(project, "/projects");
        assertThat(projectRequest.getStatus()).isEqualTo(403);
    }
}
