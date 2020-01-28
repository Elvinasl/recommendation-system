package recommendator.integration;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import recommendator.dto.BehaviorDTO;
import recommendator.dto.CellDTO;
import recommendator.models.entities.Cell;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import recommendator.models.entities.Row;

import static org.assertj.core.api.Assertions.assertThat;


public class BehaviorControllerIntegrationTest extends IntegrationTest {

    @BeforeEach
    void createClientAndLogin() throws Exception {
        createClientAndProject();
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
        assertThat(projectRequest.getStatus()).isEqualTo(403);
    }

    /**
     * Creates two new rows in the database containing some columns and values
     */
    private void insertDummyRows(){
        Project project = projectService.getByApiKey(httpHeaders.get("api-key").get(0));
        ColumnName columnTitle = insertColumnIntoDatabase("title", 70, project);
        ColumnName columnGenre = insertColumnIntoDatabase("genre", 50, project);

        Row row1 = new Row();
        row1.setProject(project);
        rowRepository.add(row1);
        insertCellIntoDatabase(columnTitle, row1, "Mr. Bean", 50);
        insertCellIntoDatabase(columnGenre, row1, "Comedy", 50);

        Row row2 = new Row();
        row2.setProject(project);
        rowRepository.add(row2);
        insertCellIntoDatabase(columnTitle, row2, "James Bond", 50);
        insertCellIntoDatabase(columnGenre, row2, "Action", 50);
    }

    /**
     * Creates and inserts a new column into the database with the given data
     * @param name of the column
     * @param weight of the column
     * @param project to add the column to
     * @return created column fetched from the database
     */
    private ColumnName insertColumnIntoDatabase(String name, int weight, Project project){
        ColumnName columnName = new ColumnName();
        columnName.setName(name);
        columnName.setWeight(weight);
        columnName.setProject(project);
        columnNameRepository.add(columnName);
        // Returning the column from the database so we have the ID etc.
        return columnNameRepository.getByNameAndProject(name, project);
    }

    /**
     * Create and insert a cell into the database
     * @param columnName to add the cell to
     * @param row to add the cell to
     * @param value of the cell
     * @param weight of the cell
     */
    private void insertCellIntoDatabase(ColumnName columnName, Row row, String value, int weight){
        Cell cell = new Cell();
        cell.setColumnName(columnName);
        cell.setRow(row);
        cell.setValue(value);
        cell.setWeight(weight);
        cellRepository.add(cell);
    }
}
