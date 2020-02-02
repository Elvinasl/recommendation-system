package recommendator.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
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
import recommendator.dto.ProjectDTO;
import recommendator.models.entities.Cell;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import recommendator.models.entities.Row;
import recommendator.repositories.*;
import recommendator.services.ClientService;
import recommendator.services.ProjectService;
import recommendator.services.RowService;

import javax.servlet.ServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
@WebAppConfiguration
@ActiveProfiles({"default", "integration"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTest {

    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    protected RowRepository rowRepository;
    @Autowired
    protected UserPreferenceRepository userPreferenceRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ColumnNameRepository columnNameRepository;
    @Autowired
    protected CellRepository cellRepository;
    @Autowired
    protected BehaviorRepository behaviorRepository;
    @Autowired
    protected ClientRepository clientRepository;
    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected RowService rowService;
    @Autowired
    protected ProjectService projectService;
    @Autowired
    protected ClientService clientService;

    protected MockMvc mockMvc;
    protected HttpHeaders httpHeaders;
    protected ObjectMapper objectMapper;
    protected final LoginDTO client = new LoginDTO("test@gmail.com", "password123");

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @AfterEach
    void cleanupDatbase() {
        behaviorRepository.deleteAll();
        userPreferenceRepository.deleteAll();
        cellRepository.deleteAll();
        columnNameRepository.deleteAll();
        rowRepository.deleteAll();
        userRepository.deleteAll();
        projectRepository.deleteAll();
        clientRepository.deleteAll();
        logout();
    }

    @Test
    public void testSetup() {
        ServletContext servletContext = wac.getServletContext();
        assertThat(servletContext).isNotNull();
        assertThat(servletContext instanceof MockServletContext).isTrue();
        assertThat(wac.getBean("clientController")).isNotNull();
    }

    /**
     * Creates a post request to the given route and returns the response
     *
     * @param body  containing the request body
     * @param route url to call
     * @return Request response
     * @throws Exception
     */
    public MockHttpServletResponse postRequest(Object body, String route) throws Exception {
        return this.mockMvc.perform(post(route)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(body)))
                .andReturn().getResponse();
    }

    /**
     * Creates a get request to the given route and returns the response
     *
     * @param route url to call
     * @return Request response
     * @throws Exception
     */
    public MockHttpServletResponse getRequest(String route) throws Exception {
        return this.mockMvc.perform(get(route)
                .headers(httpHeaders))
                .andReturn()
                .getResponse();
    }

    /**
     * Creates a delete request to the given route and returns the response
     *
     * @param route url to call
     * @return Request response
     * @throws Exception
     */
    public MockHttpServletResponse deleteRequest(String route) throws Exception {
        return this.mockMvc.perform(delete(route)
                .headers(httpHeaders))
                .andReturn()
                .getResponse();
    }

    /**
     * Creates a patch request to the given route and returns the response
     *
     * @param route url to call
     * @return Request response
     * @throws Exception
     */
    public MockHttpServletResponse patchRequest(Object body, String route) throws Exception {
        return this.mockMvc.perform(patch(route)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(body)))
                .andReturn().getResponse();
    }

    /**
     * Creates a put request to the given route and returns the response
     *
     * @param route url to call
     * @return Request response
     * @throws Exception
     */
    public MockHttpServletResponse putRequest(Object body, String route) throws Exception {
        return this.mockMvc.perform(put(route)
                .headers(httpHeaders)
                .content(objectMapper.writeValueAsString(body)))
                .andReturn().getResponse();
    }

    /**
     * Creates a login request and sets the httpheader authentication header
     *
     * @throws Exception
     */
    public void login() throws Exception {
        String bearer = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(client)))
                .andReturn().getResponse().getHeader("Authorization");

        httpHeaders.addIfAbsent("Authorization", bearer);
    }

    /**
     * Removes the Authorization header from the httpheaders
     */
    public void logout() {
        httpHeaders.remove("Authorization");
        httpHeaders.remove("api-key");
    }

    /**
     * Creates a new client, login and create a new project
     */
    public void createClientAndProject() throws Exception {
        clientService.add(client);
        login();
        String response = postRequest(new ProjectDTO("movies"), "/projects").getContentAsString();

        httpHeaders.add("api-key", new ObjectMapper().readValue(response, ProjectDTO.class).getApiKey());
    }

    /**
     * Creates two new rows in the database containing some columns and values
     */
    protected void insertDummyRows() {
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
     *
     * @param name    of the column
     * @param weight  of the column
     * @param project to add the column to
     * @return created column fetched from the database
     */
    protected ColumnName insertColumnIntoDatabase(String name, int weight, Project project) {
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
     *
     * @param columnName to add the cell to
     * @param row        to add the cell to
     * @param value      of the cell
     * @param weight     of the cell
     */
    protected void insertCellIntoDatabase(ColumnName columnName, Row row, String value, int weight) {
        Cell cell = new Cell();
        cell.setColumnName(columnName);
        cell.setRow(row);
        cell.setValue(value);
        cell.setWeight(weight);
        cellRepository.add(cell);
    }
}
