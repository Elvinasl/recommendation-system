package recommendator.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.LoginDTO;
import recommendator.repositories.ClientRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientControllerIntegrationTest extends IntegrationTest {

    @Autowired
    ClientRepository clientRepository;

    @AfterEach
    void cleanupClient(){
        clientRepository.deleteAll();
    }

    @Test
    public void register() throws Exception {
        // Creating and validating new account
        MockHttpServletResponse firstRegister = postRequest(client, "/register");
        assertThat(firstRegister.getStatus()).isEqualTo(201);
        assertThat(firstRegister.getContentAsString()).isEqualTo("{\"message\":\"Client created!\"}");

        // Trying to create the same client another time
        MockHttpServletResponse secondRegister = postRequest(client, "/register");
        assertThat(secondRegister.getStatus()).isEqualTo(409);
        assertThat(secondRegister.getContentAsString()).isEqualTo("{\"message\":\"Client with this email already exists!\"}");
    }

    @Test
    public void login() throws Exception {
        // Making sure a client exist or is created
        clientService.add(client);

        LoginDTO badClient = new LoginDTO("wrong@gmail.com", "wrongpass123");

        // Login with a valid credentials
        MockHttpServletResponse firstRegister = postRequest(client, "/login");
        assertThat(firstRegister.getStatus()).isEqualTo(200);
        assertThat(firstRegister.getHeader("Authorization")).contains("Bearer ");

        // Trying to login with bad credentials
        MockHttpServletResponse secondRegister = postRequest(badClient, "/login");
        assertThat(secondRegister.getStatus()).isEqualTo(401);
    }
}
