package recommendator.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class CellDTO {

    private Long id;

    @NotEmpty
    @NonNull
    private String columnName;

    @NotEmpty
    @NonNull
    private String value;
}
