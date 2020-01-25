package recommendator.integration;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import recommendator.dto.*;
import recommendator.models.entities.ColumnName;
import recommendator.repositories.ClientRepository;
import recommendator.repositories.ProjectRepository;
import recommendator.services.ClientService;

import javax.servlet.ServletContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ImportControllerIntegrationTest extends IntegrationTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void createClientAndLogin() throws Exception {
        createClientAndProject();
    }

    @AfterEach
    void cleanupClient(){
        projectRepository.deleteAll();
        clientRepository.deleteAll();
        logout();
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
        assertThat(projectRequest.getContentAsString()).contains("name").contains("apiKey");
    }

    private ColumnName createColumn(String name, int weight){
        ColumnName columnName = new ColumnName();
        columnName.setName(name);
        columnName.setWeight(weight);
        return columnName;
    }

    private DatasetRowDTO newRow(List<DatasetCellDTO> cellDTOS){
        DatasetRowDTO datasetRowDTO = new DatasetRowDTO();
        datasetRowDTO.setCells(cellDTOS);
        return datasetRowDTO;
    }

    private DatasetCellDTO newDatasetCellDTO(String columnName, String value, int weight){
        DatasetCellDTO datasetCellDTO = new DatasetCellDTO();
        datasetCellDTO.setColumnName(columnName);
        datasetCellDTO.setValue(value);
        datasetCellDTO.setWeight(weight);
        return datasetCellDTO;
    }
}
