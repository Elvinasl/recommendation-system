package recommendator.integration.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import recommendator.dto.LoginDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthenticationUtil {

    /**
     * Creates a login request and returns the bearer token if authenticated
     * @param client containing login information
     * @return Request response
     * @throws Exception
     */
    public static String login(MockMvc mockMvc, LoginDTO client) throws Exception {
        return mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(client)))
                .andReturn().getResponse().getHeader("Authorization");
    }
}
