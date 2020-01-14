package dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CellSeedDTO extends CellDTO {
    @Min(1)
    @Max(100)
    @NotNull
    private int weight = 50;
}
