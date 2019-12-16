package models;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * This model contains the structure that the client will use in order to create columns and/or add rows with cells to a {@link Project}
 */
@Data
public class Dataset {

    private List<ColumnName> columns;

    private List<Map<String, Cell>> rows;

}
