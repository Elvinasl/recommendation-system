package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    @Length(min = 2, max = 45)
    private String name;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String apiKey;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    @NotNull
    private Client client;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Row> rows = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ColumnName> columnNames = new ArrayList<>();

    /**
     * Add row to the project list of rows
     *
     * @param row Row to add to the list
     */
    public void addRow(Row row) {
        this.rows.add(row);
    }

    /**
     * Seed the project with data from the dataset.
     *
     * @param data This data comes from the client
     */
    public void seed(Dataset data) {

        // ==========  Create non existing columns  ==========


        // First, we make sure that we have a map from both the
        // project columns and dataset columns because project columns
        // are coming from the database and dataset columns are
        // coming from the client. We also remove the duplicates.
        Map<String, ColumnName> headers = Stream.of(data.getColumns(), this.getColumnNames())
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toMap(ColumnName::getName, cn -> cn, (ColumnName x, ColumnName y) -> y));

        // The project requires the ColumnNames as a list. So we parse it.
        List<ColumnName> columns = new ArrayList<>(headers.values());
        this.setColumnNames(columns);

        // For each column we set the project.
        columns.forEach((column) -> column.setProject(this));


        // ==========  Create cell and row data  ==========


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
                dbRow.setProject(this);

                // Add this new row to this project
                this.addRow(dbRow);

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
