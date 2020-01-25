package recommendator.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import recommendator.dto.AllClientsDTO;
import recommendator.dto.ClientDTO;
import recommendator.dto.LoginDTO;
import recommendator.exceptions.SomethingWentWrongException;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.repositories.ClientRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService  {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Response add(LoginDTO loginDTO) {
        try {
            clientRepository.getByEmail(loginDTO.getEmail());
        } catch (NoResultException e) {

            Client client = new Client();
            client.setEmail(loginDTO.getEmail());
            // encrypting password
            client.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
            client.setRole("ROLE_USER");
            clientRepository.add(client);
            return new Response("Client created!");
        }

        throw new SomethingWentWrongException("Client with this email already exists!");
    }

    public Response makeAdmin(Long clientId) {
        Client client = getById(clientId);
        client.setRole("ROLE_ADMIN");
        clientRepository.update(client);
        return new Response("Client " + client.getEmail() + " is now admin!");
    }

    public Response disable(Long clientId) {
        Client client = getById(clientId);
        client.setActivated(false);
        clientRepository.update(client);
        return new Response("Client deleted!");
    }

    private Client getById(Long clientId) {
        return clientRepository.getById(clientId);
    }

    public AllClientsDTO getAllClients() {
        List<ClientDTO> clients = clientRepository.getAllByActivated(true)
                .stream()
                .map(client -> new ClientDTO(client.getId(), client.getEmail(), client.getRole()))
                .collect(Collectors.toList());
        return new AllClientsDTO(clients);
    }
}
