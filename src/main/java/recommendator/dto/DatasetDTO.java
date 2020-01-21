package recommendator.dto;

import lombok.Data;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;

import java.util.List;

/**
 * This model contains the structure that the client will use in order to create columns and/or add rows with cells to a {@link Project}
 */
@Data
public class DatasetDTO {

    private List<ColumnName> columns;

    private List<DatasetRowDTO> rows;

}
