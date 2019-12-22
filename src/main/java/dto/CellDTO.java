package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellDTO {

    @Min(1)
    @Max(100)
    @NotNull
    private int weight = 50;

    @NotEmpty
    private String columnName;

    @NotEmpty
    private String value;
}
