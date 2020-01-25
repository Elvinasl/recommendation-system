package recommendator.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
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
import recommendator.repositories.ClientRepository;
import recommendator.services.ClientService;

import javax.servlet.ServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class, DatabaseConfig.class})
@WebAppConfiguration
@ActiveProfiles({"default", "integration"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientService clientService;
    private MockMvc mockMvc;
    private HttpHeaders httpHeaders;
    private ObjectMapper objectMapper;
    private LoginDTO client;

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        objectMapper = new ObjectMapper();
        client = new LoginDTO("test@gmail.com","password123");
    }

    @Test
    public void testSetup(){
        ServletContext servletContext = wac.getServletContext();
        assertThat(servletContext).isNotNull();
        assertThat(servletContext instanceof MockServletContext).isTrue();
        assertThat(wac.getBean("clientController")).isNotNull();
    }

    @Test
    public void request() throws Exception {
        // Creating and validating new account
        MockHttpServletResponse firstRegister = request(client, "/register");
        assertThat(firstRegister.getStatus()).isEqualTo(201);
        assertThat(firstRegister.getContentAsString()).isEqualTo("{\"message\":\"Client created!\"}");

        // Trying to create the same client another time
        MockHttpServletResponse secondRegister = request(client, "/register");
        assertThat(secondRegister.getStatus()).isEqualTo(409);
        assertThat(secondRegister.getContentAsString()).isEqualTo("{\"message\":\"Client with this email already exists!\"}");

        // Cleanup
        clientRepository.remove(1);
    }

    @Test
    public void login() throws Exception {
        // Making sure a client exist or is created
        clientService.add(client);

        LoginDTO badClient = new LoginDTO("wrong@gmail.com", "wrongpass123");

        // Login with a valid credentials
        MockHttpServletResponse firstRegister = request(client, "/login");
        assertThat(firstRegister.getStatus()).isEqualTo(200);
        assertThat(firstRegister.getHeader("Authorization")).contains("Bearer ");

        // Trying to login with bad credentials
        MockHttpServletResponse secondRegister = request(badClient, "/login");
        assertThat(secondRegister.getStatus()).isEqualTo(401);

        // Cleanup
        clientRepository.remove(1);
    }

    /**
     * Creates a post reqequest to the given route and returns the response
     * @param client containing login information
     * @param route url tou call
     * @return Request response
     * @throws Exception
     */
    private MockHttpServletResponse request(LoginDTO client, String route) throws Exception {
        return this.mockMvc.perform(post(route)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(client)))
                .andReturn().getResponse();
    }
}
