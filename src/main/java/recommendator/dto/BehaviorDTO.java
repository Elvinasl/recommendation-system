package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * This DTO holds information about a user behavior. It can be seen as a like/dislike.
 * It contains a list of {@link CellDTO} that together form a unique combination for a single row.
 * A userID to know which user liked/dislike something and a boolean to specify the liked/disliked.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BehaviorDTO {

    private List<CellDTO> cells;
    @NotNull
    private boolean liked;
    @NotNull
    private String userId;
}
