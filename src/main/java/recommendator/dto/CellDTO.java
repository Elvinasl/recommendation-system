package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellDTO {

    private Long id;

    @NotEmpty
    private String columnName;

    @NotEmpty
    private String value;
}
