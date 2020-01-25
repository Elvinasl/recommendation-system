package recommendator.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectDTO {

    @NonNull
    @NotEmpty
    @Length(min = 2, max = 45)
    private String name;

    private String apiKey;
}
