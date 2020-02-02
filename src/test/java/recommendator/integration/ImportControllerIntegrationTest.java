package recommendator.integration;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.DatasetCellDTO;
import recommendator.dto.DatasetDTO;
import recommendator.dto.DatasetRowDTO;
import recommendator.models.entities.ColumnName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ImportControllerIntegrationTest extends IntegrationTest {

    @BeforeEach
    void createClientAndLogin() throws Exception {
        createClientAndProject();
    }

    @Test
    public void importDataset() throws Exception {
        DatasetDTO datasetDTO = new DatasetDTO();
        datasetDTO.setColumns(Lists.list(
                createColumn("title", 70),
                createColumn("genre", 50)
        ));
        datasetDTO.setRows(Lists.list(
                newRow(Lists.list(
                        newDatasetCellDTO("title", "Mr. Bean", 50),
                        newDatasetCellDTO("genre", "Humor", 50)
                )),
                newRow(Lists.list(
                        newDatasetCellDTO("title", "Spiderman", 50),
                        newDatasetCellDTO("genre", "Cartoon", 50)
                ))
        ));

        // Creating a new project and validating if a api-key is handed back
        MockHttpServletResponse projectRequest = postRequest(datasetDTO, "/import");
        assertThat(projectRequest.getStatus()).isEqualTo(201);
        assertThat(projectRequest.getContentAsString()).isEqualTo("{\"message\":\"Data has been added\"}");

        // Inserting the same row again (should give a duplicate row exception)
        projectRequest = postRequest(datasetDTO, "/import");
        assertThat(projectRequest.getStatus()).isEqualTo(409);
        assertThat(projectRequest.getContentAsString()).isEqualTo("{\"message\":\"Row duplicate found for row: Mr. Bean, Humor\"}");

    }

    /**
     * Creates a new column with the given data
     *
     * @param name   of the column
     * @param weight of the column
     * @return New ColumnName object
     */
    private ColumnName createColumn(String name, int weight) {
        ColumnName columnName = new ColumnName();
        columnName.setName(name);
        columnName.setWeight(weight);
        return columnName;
    }

    private DatasetRowDTO newRow(List<DatasetCellDTO> cellDTOS) {
        DatasetRowDTO datasetRowDTO = new DatasetRowDTO();
        datasetRowDTO.setCells(cellDTOS);
        return datasetRowDTO;
    }

    private DatasetCellDTO newDatasetCellDTO(String columnName, String value, int weight) {
        DatasetCellDTO datasetCellDTO = new DatasetCellDTO();
        datasetCellDTO.setColumnName(columnName);
        datasetCellDTO.setValue(value);
        datasetCellDTO.setWeight(weight);
        return datasetCellDTO;
    }
}
