package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * DatasetCellDTO is the same as a {@link CellDTO} but has weight added to it.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatasetCellDTO extends CellDTO {

    @Min(1)
    @Max(100)
    @NotNull
    private int weight = 50;
}
