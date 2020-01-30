package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This DTO is holding information about a client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private long id;
    private String email;
    private String role;
}
