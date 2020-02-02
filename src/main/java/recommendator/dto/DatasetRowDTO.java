package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This DTO contains a list of {@link DatasetCellDTO} to create a combination of cells that form a {@link recommendator.models.entities.Row}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatasetRowDTO {
    private List<DatasetCellDTO> cells;
}
