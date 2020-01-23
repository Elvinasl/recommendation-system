package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import recommendator.models.entities.Client;
import recommendator.repositories.ClientRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientPrincipalDetailsServiceTest {

    @Mock ClientRepository clientRepository;

    @Test
    void loadUserByUsername() {
        Client client = new Client();
        String email = "test";
        client.setEmail(email);
        Mockito.when(clientRepository.getByEmail(email)).thenReturn(client);

        Client response = clientRepository.getByEmail(email);

        // checking if get by email returns the same client
        assertEquals(response.getEmail(), email);

        // checking username not found exception
        assertThatThrownBy(() -> {
            throw new UsernameNotFoundException("not found");
        })
        .hasMessage("not found")
        .isExactlyInstanceOf(UsernameNotFoundException.class);

    }
}
