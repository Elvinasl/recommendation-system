package recommendator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recommendator.dto.DatasetDTO;
import recommendator.dto.DatasetRowDTO;
import recommendator.dto.ProjectDTO;
import recommendator.dto.ReturnObjectDTO;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Client;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import recommendator.repositories.ProjectRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private ColumnNameService columnNameService;
    private RowService rowService;
    private final ClientPrincipalDetailsService clientPrincipalDetailsService;

    @Autowired
    public ProjectService(
            ProjectRepository projectRepository,
            ColumnNameService columnNameService,
            RowService rowService,
            ClientPrincipalDetailsService clientService) {
        this.projectRepository = projectRepository;
        this.columnNameService = columnNameService;
        this.rowService = rowService;
        this.clientPrincipalDetailsService = clientService;
    }

    /**
     * Generate api key for the {@link Project} and persist it to the database
     *
     * @param projectDTO The project where to create api key for
     * @return projectDTO containing the name and api-key
     */
    public ProjectDTO add(ProjectDTO projectDTO) {
        String clientUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientPrincipalDetailsService.getClientByUsername(clientUsername);

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setClient(client);
        project.setApiKey(UUID.randomUUID().toString());

        // Add project
        projectRepository.add(project);

        return new ProjectDTO(project.getName(), project.getApiKey());
    }

    /**
     * Get projects for the client from the database
     *
     * @return List of projectDTOs containing the name and api-key
     */
    public ReturnObjectDTO<ProjectDTO> listByCurrentClient() {
        String clientUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientPrincipalDetailsService.getClientByUsername(clientUsername);

        List<Project> projects = projectRepository.listByClient(client);

//        Map<Integer, ProjectDTO> projectDTOMap = new HashMap<>();
//        projects.forEach(project -> {
//            projectDTOMap.put(,, new ProjectDTO(project.getName(), project.getApiKey()));
//        });
//
        List<ProjectDTO> projectDTOS = projects.stream()
                .map(project -> new ProjectDTO(project.getName(), project.getApiKey()))
                .collect(Collectors.toList());



        return new ReturnObjectDTO<>(projectDTOS);
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
