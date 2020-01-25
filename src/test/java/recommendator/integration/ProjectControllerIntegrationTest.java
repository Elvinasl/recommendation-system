package recommendator.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import recommendator.dto.ProjectDTO;
import recommendator.integration.utils.AuthenticationUtil;
import recommendator.repositories.ClientRepository;
import recommendator.repositories.ProjectRepository;
import recommendator.services.ClientService;

import javax.servlet.ServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



public class ProjectControllerIntegrationTest extends SetupIntegrationTest{

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ClientService clientService;

    @BeforeEach
    void createClientAndLogin() throws Exception {
        clientService.add(client);
        String bearer = AuthenticationUtil.login(mockMvc, client);
        httpHeaders.add("Authorization", bearer);
    }

    @AfterEach
    void cleanupClient(){
        projectRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testSetup(){
        ServletContext servletContext = wac.getServletContext();
        assertThat(servletContext).isNotNull();
        assertThat(servletContext instanceof MockServletContext).isTrue();
        assertThat(wac.getBean("clientController")).isNotNull();
    }

    @Test
    public void createProject() throws Exception {
        ProjectDTO project = new ProjectDTO("testProject");
        // Creating a new project and validating if a api-key is handed back
        MockHttpServletResponse projectRequest = request(project, "/project");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThat(projectRequest.getContentAsString()).contains("name").contains("apiKey");

        // Creating a project without being authorized
        httpHeaders.remove("Authorization");
        projectRequest = request(project, "/project");
        assertThat(projectRequest.getStatus()).isEqualTo(403);
    }

    /**
     * Creates a post request to the given route and returns the response
     * @param project containing the name of the project
     * @param route url to call
     * @return Request response
     * @throws Exception
     */
    private MockHttpServletResponse request(ProjectDTO project, String route) throws Exception {
        return this.mockMvc.perform(post(route)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse();
    }
}
