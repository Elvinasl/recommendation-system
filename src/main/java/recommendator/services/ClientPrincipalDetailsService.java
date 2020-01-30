package recommendator.services;

import recommendator.config.security.ClientPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recommendator.models.entities.Client;
import recommendator.repositories.ClientRepository;

/**
 * This service contains all the logic for everything that has something to do with ClientPrincipalDetails.
 */
@Service
public class ClientPrincipalDetailsService implements UserDetailsService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientPrincipalDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new ClientPrincipal(getClientByUsername(email));
    }

    /**
     * Grabs the client object from the database by a given email
     * @param email of the client
     * @return client object
     */
    public Client getClientByUsername(String email){
        return this.clientRepository.getByEmail(email);
    }
}
