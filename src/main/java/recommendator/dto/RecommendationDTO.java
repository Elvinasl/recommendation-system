package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * This DTO is used by clients to request recommendations
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendationDTO {

    @NotEmpty
    private String userId;
    @Min(1)
    @Max(10)
    @NotNull
    private int amount;

}
