package recommendator.integration;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.LoginDTO;
import recommendator.models.entities.Client;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientControllerIntegrationTest extends IntegrationTest {

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
        clientService.add(client);
        super.login();

        LoginDTO badClient = new LoginDTO("wrong@gmail.com", "wrongpass123");

        // Login with a valid credentials
        MockHttpServletResponse firstRegister = postRequest(client, "/login");
        assertThat(firstRegister.getStatus()).isEqualTo(200);
        assertThat(firstRegister.getHeader("Authorization")).contains("Bearer ");

        // Trying to login with bad credentials
        MockHttpServletResponse secondRegister = postRequest(badClient, "/login");
        assertThat(secondRegister.getStatus()).isEqualTo(401);
    }

    @Test
    public void makeAdmin() throws Exception {
        clientService.add(client);
        super.login();
        createDummyClient();
        // Making sure a client exist or is created
        setRole("ADMIN");

        Long id = clientRepository.getByEmail("dummyUser@email.com").getId();
        // Make client admin
        MockHttpServletResponse request = putRequest("/admin/" + id);
        assertThat(request.getStatus()).isEqualTo(200);

        // Trying to make admin without being admin
        setRole("USER");
        request = putRequest(client, "/admin/" + id);
        assertThat(request.getStatus()).isEqualTo(403);
    }

    @Test
    public void disableUser() throws Exception {
        clientService.add(client);
        super.login();
        createDummyClient();
        setRole("ADMIN");

        Long id = clientRepository.getByEmail("dummyUser@email.com").getId();
        // Delete client admin
        MockHttpServletResponse request = deleteRequest("/admin/" + id);
        assertThat(request.getStatus()).isEqualTo(200);

        // Trying to make delete without being admin
        setRole("USER");
        request = putRequest(client, "/admin/" + id);
        assertThat(request.getStatus()).isEqualTo(403);
    }

    @Test
    public void getAllUsers() throws Exception {
        clientService.add(client);
        super.login();
        createDummyClient();
        setRole("ADMIN");

        // Get all users
        MockHttpServletResponse request = getRequest("/admin/");
        assertThat(request.getStatus()).isEqualTo(200);
        assertThat(request.getContentAsString()).contains("test@gmail.com").contains("dummyUser@email.com");

        // Trying to get all users without being admin
        setRole("USER");
        request = getRequest("/admin/");
        assertThat(request.getStatus()).isEqualTo(403);
    }

    private void setRole(String role){
        Client clientObj = clientRepository.getByEmail(client.getEmail());
        clientObj.setRole(role);
        clientRepository.update(clientObj);
    }

    private void createDummyClient(){
        LoginDTO dummyClient = new LoginDTO();
        dummyClient.setEmail("dummyUser@email.com");
        dummyClient.setPassword("pass");
        clientService.add(dummyClient);
    }
}
