package dto;

import lombok.Data;
import models.Cell;
import models.ColumnName;
import models.Project;
import models.Row;

import java.util.List;
import java.util.Map;

/**
 * This model contains the structure that the client will use in order to create columns and/or add rows with cells to a {@link Project}
 */
@Data
public class DatasetDTO {

    private List<ColumnName> columns;

    private List<RowDTO> rows;

}
