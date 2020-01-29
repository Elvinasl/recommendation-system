package recommendator.exceptions.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: We can remove this class I think and use Response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowExistsResponse {

    private String message;
}
