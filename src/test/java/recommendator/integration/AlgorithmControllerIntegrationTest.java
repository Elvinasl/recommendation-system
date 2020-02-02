package recommendator.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.ProjectDTO;
import recommendator.exceptions.NotFoundException;
import recommendator.models.entities.Project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AlgorithmControllerIntegrationTest extends IntegrationTest {

    @BeforeEach
    void createClientAndLogin() throws Exception {
        createClientAndProject();
        insertDummyRows();
    }

    @Test
    public void recommend() throws Exception {
        // Getting recommendations without a specific user
        MockHttpServletResponse projectRequest = getRequest("/recommendation");
        assertThat(projectRequest.getStatus()).isEqualTo(200);
        assertThat(projectRequest.getContentAsString()).contains("rows").contains("columnName");
    }
}
