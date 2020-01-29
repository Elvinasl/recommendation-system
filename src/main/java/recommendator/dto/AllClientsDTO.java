package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This DTO holds a list of all clients
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllClientsDTO {
    private List<ClientDTO> clients;
}
