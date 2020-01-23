package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.models.entities.Project;
import recommendator.models.entities.User;
import recommendator.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;

    @InjectMocks UserService userService;

    @Test
    void add() {
        User user = new User();
        user.setExternalUserId("id");

        Mockito.when(userRepository.findByExternalIdAndProjectOrNull(anyString(), any(Project.class))).thenReturn(user);

        User response = userService.findByExternalIdAndProjectOrNull("", new Project());

        // checking whether user was mapped correctly
        assertThat(response).isEqualTo(user);
        assertThat(response.getExternalUserId()).isEqualTo(user.getExternalUserId());

    }

    @Test
    void findByExternalIdAndProjectOrNull() {
    }
}
