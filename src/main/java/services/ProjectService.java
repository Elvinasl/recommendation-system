package services;

import dto.Dataset;
import exceptions.responses.Response;
import models.Cell;
import models.ColumnName;
import models.Project;
import models.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ProjectRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
    public Response seedDatabase(String apiKey, Dataset data) {

        // We first need to get the project by the given api key
        Project project = this.getByApiKey(apiKey);

        // Then we seed the project with the given dataset
        this.seed(data, project);

        // Update project with all their newly created columns/cells/rows
        projectRepository.update(project);

        return new Response("Created");
    }

    /**
     * Seed the project with data from the dataset.
     *
     * @param data This data comes from the client
     */
    public void seed(Dataset data, Project project) {

        // ==========  Create non existing columns  ==========


        // First, we make sure that we have a map from both the
        // project columns and dataset columns because project columns
        // are coming from the database and dataset columns are
        // coming from the client. We also remove the duplicates.

        Map<String, ColumnName> headers = Stream.of(data.getColumns(), project.getColumnNames())
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toMap(ColumnName::getName, cn -> cn, (ColumnName x, ColumnName y) -> y));

        // The project requires the ColumnNames as a list. So we parse it.
        List<ColumnName> columns = new ArrayList<>(headers.values());
        project.setColumnNames(columns);

        // For each column we set the project.
        columns.forEach((column) -> column.setProject(project));

        this.createCellWithData(data, project, headers);

    }

    public void createCellWithData(Dataset data, Project project, Map<String, ColumnName> headers) {
        // We create a map of cells to prevent duplicates
        Map<String, Cell> cells = new HashMap<>();

        // Get rows from dataset because these rows aren't
        // an instance of Row model.
        List<Map<String, Cell>> rows = data.getRows();

        if (rows != null) {

            // Now we want to loop trough all the rows in the dataset
            rows.forEach((row) -> {

                // For each dataset row we create a new row that will be
                // inserted into the database because the 'row'-variable
                // is an map of string(column name) and cell
                Row dbRow = new Row();

                // This new row should know to which project it belongs to
                dbRow.setProject(project);

                // Add this new row to this project
                project.addRow(dbRow);

                // Now we will loop trough all the cells from the current
                // row with their column name as key
                row.forEach((columnName, cell) -> {

                    // To prevent an exception we need to make sure that the column name exists
                    if (headers.containsKey(columnName)) {

                        // Now we want to retrieve the ColumnName object
                        // from the headers by the column name key
                        ColumnName col = headers.get(columnName);

                        // Does cell exists?
                        if (cells.containsKey(cell.getValue())) {
                            // We can just take the existing cell from the map
                            cell = cells.get(cell.getValue());
                        } else {
                            // We need to add the cell to the map
                            cells.put(cell.getValue(), cell);

                            // Set the columnName object for cell
                            cell.setColumnName(col);

                            // Add cell to the columnName object
                            col.addCell(cell);
                        }

                        // Add row to cell
                        cell.addRow(dbRow);

                        // Add cell to row
                        dbRow.addCell(cell);
                    }
                });
            });
        }
    }

}
