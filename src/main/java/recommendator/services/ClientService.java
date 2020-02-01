package recommendator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recommendator.dto.AllClientsDTO;
import recommendator.dto.ClientDTO;
import recommendator.dto.LoginDTO;
import recommendator.exceptions.SomethingWentWrongException;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Client;
import recommendator.repositories.ClientRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This service contains all the logic for everything that has something to do with Clients.
 */
@Service
public class ClientService {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new {@link Client} if the requested client does not yet exist
     *
     * @param loginDTO containing login information
     * @return message with the status
     */
    public Response add(LoginDTO loginDTO) {
        try {
            clientRepository.getByEmail(loginDTO.getEmail());
        } catch (NoResultException e) {

            Client client = new Client();
            client.setEmail(loginDTO.getEmail());
            // encrypting password
            client.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
            client.setRole("USER");
            clientRepository.add(client);
            return new Response("Client created!");
        }

        throw new SomethingWentWrongException("Client with this email already exists!");
    }

    /**
     * Makes the {@link Client} with a specific ID Admin.
     *
     * @param clientId of the client that should be made admin.
     * @return message with the status
     */
    public Response makeAdmin(Long clientId) {
        Client client = getById(clientId);
        client.setRole("ADMIN");
        clientRepository.update(client);
        return new Response("Client " + client.getEmail() + " is now admin!");
    }

    /**
     * Disables a {@link Client} with a specific ID.
     *
     * @param clientId of the client that should be disabled
     * @return message with the status
     */
    public Response disable(Long clientId) {
        Client client = getById(clientId);
        client.setActivated(false);
        clientRepository.update(client);
        return new Response("Client deleted!");
    }

    /**
     * Gathers a {@link Client} with a specific ID.
     *
     * @param clientId to find
     * @return the client that has bin found
     */
    private Client getById(Long clientId) {
        return clientRepository.getById(clientId);
    }

    /**
     * Gathers all the clients and returns them as a DTO.
     *
     * @return DTO of all clients
     */
    public AllClientsDTO getAllClients() {
        List<ClientDTO> clients = clientRepository.getAllByActivated(true)
                .stream()
                .map(client -> new ClientDTO(client.getId(), client.getEmail(), client.getRole()))
                .collect(Collectors.toList());
        return new AllClientsDTO(clients);
    }
}
