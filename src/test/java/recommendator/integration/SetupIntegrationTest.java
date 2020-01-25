package recommendator.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class, DatabaseConfig.class})
@WebAppConfiguration
@ActiveProfiles({"default", "integration"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class SetupIntegrationTest {

    @Autowired
    protected WebApplicationContext wac;
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
    }
}
