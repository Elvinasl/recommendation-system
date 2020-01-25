package recommendator.services;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import recommendator.dto.LoginDTO;
import recommendator.exceptions.RowAlreadyExistsException;
import recommendator.exceptions.SomethingWentWrongException;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.repositories.ClientRepository;

import javax.persistence.NoResultException;

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

        throw new RowAlreadyExistsException("Client with this email already exists!");
    }
}
