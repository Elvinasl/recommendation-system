package recommendator.services;

import recommendator.config.security.ClientPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recommendator.models.entities.Client;
import recommendator.repositories.ClientRepository;

@Service
public class ClientPrincipalDetailsService implements UserDetailsService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientPrincipalDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = this.clientRepository.getByEmail(email);
        return new ClientPrincipal(client);
    }
}
