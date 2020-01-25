package recommendator.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import recommendator.exceptions.SomethingWentWrongException;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Client;
import recommendator.repositories.ClientRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock ClientRepository clientRepository;

    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks ClientService clientService;

    @Test
    void add() {
        // creating a fake client
        String email = "email@email.com";
        Client client = new Client();
        client.setEmail(email);
        client.setPassword("password");

        // if client doesn't exists, we allow to register
        Mockito.when(clientRepository.getByEmail(email)).thenThrow(EmptyResultDataAccessException.class);
        Mockito.when(clientRepository.add(any(Client.class))).thenReturn(client);

        // creating a client
        Response response = clientService.add(client);

        // checking the exception
        assertThatThrownBy(() -> clientService
                .add(new Client(1L, "m", null, null, true, null)))
                .isExactlyInstanceOf(SomethingWentWrongException.class)
                .hasMessage("Client with this email already exists!");

        // verifying that password was encrypted and client was added (catch block was executed"
        verify(clientRepository, times(1)).add(client);
        verify(passwordEncoder, times(1)).encode("password");
        Assertions.assertThat(response.getMessage()).isEqualTo("Client created!");
    }
}
