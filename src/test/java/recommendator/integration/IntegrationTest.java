package recommendator.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import recommendator.config.AppConfig;
import recommendator.config.DatabaseConfig;
import recommendator.dto.LoginDTO;
import recommendator.dto.ProjectDTO;
import recommendator.models.entities.Client;
import recommendator.services.ClientService;
import recommendator.services.ProjectService;

import javax.servlet.ServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class, DatabaseConfig.class})
@WebAppConfiguration
@ActiveProfiles({"default", "integration"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTest {

    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    protected ClientService clientService;
    @Autowired
    protected ProjectService projectService;
    protected MockMvc mockMvc;
    protected HttpHeaders httpHeaders;
    protected ObjectMapper objectMapper;
    protected final LoginDTO client = new LoginDTO("test@gmail.com","password123");

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Test
    public void testSetup(){
        ServletContext servletContext = wac.getServletContext();
        assertThat(servletContext).isNotNull();
        assertThat(servletContext instanceof MockServletContext).isTrue();
        assertThat(wac.getBean("clientController")).isNotNull();
    }

    /**
     * Creates a post request to the given route and returns the response
     * @param body containing the request body
     * @param route url to call
     * @return Request response
     * @throws Exception
     */
    public MockHttpServletResponse postRequest(Object body, String route) throws Exception {
        return this.mockMvc.perform(post(route)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(body)))
                .andReturn().getResponse();
    }

    /**
     * Creates a login request and sets the httpheader authentication header
     * @throws Exception
     */
    public void login() throws Exception {
          String bearer = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(client)))
                .andReturn().getResponse().getHeader("Authorization");

          httpHeaders.addIfAbsent("Authorization", bearer);
    }

    /**
     * Removes the Authorization header from the httpheaders
     */
    public void logout(){
        httpHeaders.remove("Authorization");
        httpHeaders.remove("api-key");
    }

    /**
     * Creates a new client, login and create a new project
     */
    public void createClientAndProject() throws Exception {
        clientService.add(client);
        login();
        String response = postRequest(new ProjectDTO("movies"), "/projects").getContentAsString();

        httpHeaders.add("api-key", new ObjectMapper().readValue(response, ProjectDTO.class).getApiKey());
    }
}
