package recommendator.integration;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.CellDTO;
import recommendator.dto.RowDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class RowControllerIntegrationTest extends IntegrationTest {

    @BeforeEach
    void createClientAndLogin() throws Exception {
        createClientAndProject();
        insertDummyRows();
    }

    @Test
    public void getRows() throws Exception {
        String apiKey = httpHeaders.get("api-key").get(0);
        // Requesting all the rows
        MockHttpServletResponse request = getRequest("/projects/" + apiKey + "/rows");
        assertThat(request.getStatus()).isEqualTo(200);
        // We cannot check the whole json because ID's are different every test
        assertThat(request.getContentAsString()).contains("objects").contains("columnName").contains("Comedy");
        // Requesting all the rows again but this time with a wrong api-key
        request = getRequest("/projects/" + "wrong api key" + "/rows");
        assertThat(request.getStatus()).isEqualTo(200);
        assertThat(request.getContentAsString()).isEqualTo("{\"objects\":[]}");

        // Requesting all rows without being authenticated
        logout();
        request = getRequest("/projects/" + apiKey + "/rows");
        assertThat(request.getStatus()).isEqualTo(403);
    }

    @Test
    public void deleteRow() throws Exception {
        String apiKey = httpHeaders.get("api-key").get(0);
        RowDTO row1 = (RowDTO) rowService.getByApiKey(apiKey).getObjects().get(0);

        // Delete a row with an existing ID
        MockHttpServletResponse request = deleteRequest("/rows/" + row1.getId());
        assertThat(request.getStatus()).isEqualTo(200);
        assertThat(request.getContentAsString()).isEqualTo("{\"message\":\"Row deleted!\"}");
        // Requesting to delete a row without being authenticated
        logout();
        request = deleteRequest("/rows/" + row1.getId());
        assertThat(request.getStatus()).isEqualTo(403);
    }

    @Test
    public void updateRow() throws Exception {
        String apiKey = httpHeaders.get("api-key").get(0);
        RowDTO row1 = (RowDTO) rowService.getByApiKey(apiKey).getObjects().get(0);

        RowDTO newRow = new RowDTO();
        List<CellDTO> cells = Lists.list(
                new CellDTO(row1.getCells().get(0).getId(), "title", "new"));
        newRow.setCells(cells);

        // Update a row
        MockHttpServletResponse request = putRequest(newRow, "/rows/" + row1.getId());
        assertThat(request.getStatus()).isEqualTo(200);
        assertThat(request.getContentAsString()).isEqualTo("{\"message\":\"Row updated!\"}");
        // Requesting to update a row without being authenticated
        logout();
        request = putRequest(newRow, "/rows/" + row1.getId());
        assertThat(request.getStatus()).isEqualTo(403);
    }
}
