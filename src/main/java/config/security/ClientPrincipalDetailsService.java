package config.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import models.Client;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import repositories.ClientRepository;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ClientPrincipalDetailsService implements UserDetailsService {
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = this.clientRepository.getByEmail(email);
        return new ClientPrincipal(client);
    }
}
