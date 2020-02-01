package recommendator.integration;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.BehaviorDTO;
import recommendator.dto.CellDTO;

import static org.assertj.core.api.Assertions.assertThat;


public class BehaviorControllerIntegrationTest extends IntegrationTest {

    @BeforeEach
    void createClientAndLogin() throws Exception {
        createClientAndProject();
        insertDummyRows();
    }

    @Test
    public void insertBehavior() throws Exception {
        insertDummyRows();

        BehaviorDTO behaviorDTO = new BehaviorDTO();
        behaviorDTO.setCells(Lists.list(
                new CellDTO("title", "Mr. Bean"),
                new CellDTO("genre", "Comedy")
        ));
        behaviorDTO.setLiked(true);
        behaviorDTO.setUserId("user1");

        // Insert behavior and check the result
        MockHttpServletResponse projectRequest = postRequest(behaviorDTO, "/behavior");
        assertThat(projectRequest.getStatus()).isEqualTo(202);
        assertThat(projectRequest.getContentAsString()).isEqualTo("{\"message\":\"Behavior recorded\"}");

        // Insert behavior without api-key
        logout();
        projectRequest = postRequest(behaviorDTO, "/behavior");
        assertThat(projectRequest.getStatus()).isEqualTo(400);
    }
}
