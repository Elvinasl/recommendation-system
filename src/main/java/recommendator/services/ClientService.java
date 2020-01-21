package recommendator.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import recommendator.models.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.repositories.ClientRepository;

@Service
public class ClientService  {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client add(Client client) {
        // encrypting password
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.add(client);
    }
}
