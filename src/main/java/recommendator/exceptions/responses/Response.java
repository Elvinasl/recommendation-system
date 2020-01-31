package recommendator.exceptions.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response is used to sent simple messages to the client. If it's used
 * with a {@link org.springframework.http.ResponseEntity} it will create a nice formatted JSON
 * containing a message.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String message;
}
