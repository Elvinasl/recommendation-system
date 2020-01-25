package recommendator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.dto.DatasetDTO;
import recommendator.dto.DatasetRowDTO;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import recommendator.repositories.ProjectRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private ColumnNameService columnNameService;
    private RowService rowService;

    @Autowired
    public ProjectService(
            ProjectRepository projectRepository,
            ColumnNameService columnNameService,
            RowService rowService
    ) {
        this.projectRepository = projectRepository;
        this.columnNameService = columnNameService;
        this.rowService = rowService;
    }

    /**
     * Generate api key for the {@link Project} and persist it to the database
     *
     * @param project The project where to create api key for
     * @return the generated api key
     */
    public String add(Project project) {
        // Generate api key
        String apiKey = UUID.randomUUID().toString();

        // Set api key
        project.setApiKey(apiKey);

        // Add project
        projectRepository.add(project);

        // Return api key
        return apiKey;
    }

    /**
     * Gets the project belonging to the given api key
     *
     * @param key The api key to find project for
     * @return {@link Project}
     */
    public Project getByApiKey(String key) {
        return projectRepository.getByApiKey(key);
    }

    /**
     * Seeds the database with the given dataset
     *
     * @param apiKey used to get the project
     * @param data   containing project information
     */
    public Response seedDatabase(String apiKey, DatasetDTO data) {

        // We first need to get the project by the given api key
        Project project = this.getByApiKey(apiKey);

        // Then we seed the project with the given dataset
        this.seed(data, project);

        return new Response("Data has been added");
    }

    /**
     * Seed the project with containers from the datasetDTO.
     *
     * @param datasetDTO This containers comes from the client
     * @param project this is the project which should be seeded
     */
    public void seed(DatasetDTO datasetDTO, Project project) {

        List<ColumnName> columnNames = datasetDTO.getColumns();
        List<DatasetRowDTO> datasetRowDTOs = datasetDTO.getRows();
        // Handling the columns
        if(columnNames != null) {
            columnNames.forEach(columnName -> columnNameService.addOrUpdate(columnName, project));
        }

        // Handling the rows
        if (datasetRowDTOs != null) {
            datasetRowDTOs.forEach(row -> rowService.addOrUpdate(row.getCells(), project));

        }
    }
}
