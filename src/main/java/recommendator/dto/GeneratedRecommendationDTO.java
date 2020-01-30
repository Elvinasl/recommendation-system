package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This DTO contains all the {@link RowDTO}'s that are being recommended to a user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedRecommendationDTO {

    private List<RowDTO> rows;

}
