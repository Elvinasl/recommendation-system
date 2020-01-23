package recommendator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.dto.BehaviorDTO;
import recommendator.dto.CellDTO;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Project;
import recommendator.models.entities.Row;
import recommendator.models.entities.User;
import recommendator.repositories.BehaviorRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class BehaviorServiceTest {

    @Mock ProjectService projectService;
    @Mock RowService rowService;
    @Mock UserService userService;
    @Mock UserPreferenceService userPreferenceService;
    @Mock BehaviorRepository behaviorRepository;
    @InjectMocks BehaviorService behaviorService;

    @Test
    void add() {

        Mockito.when(projectService.getByApiKey(anyString())).thenReturn(new Project());
        Mockito.when(rowService.getRowByCellDTOAndProject(anyList(), any(Project.class))).thenReturn(new Row());
        // if we are creating behavior with existing user, we need user,
        // if we are creating with new (external) user we want to create new (external) user
        Mockito.when(userService.findByExternalIdAndProjectOrNull(anyString(), any(Project.class))).thenReturn(new User()).thenReturn(null);
        Response response = behaviorService.add("test", new BehaviorDTO(new ArrayList<>(),false, "userId"));
        assertThat("Behavior recorded").isEqualTo(response.getMessage());
        verify(behaviorRepository, times(1)).add(any(Behavior.class));

        // Checking if the user is null
        response = behaviorService.add("test", new BehaviorDTO(new ArrayList<>(),false, "userId"));
        assertThat("Behavior recorded").isEqualTo(response.getMessage());
        // if it is a new user, this method should be called
        verify(userService, times(1)).add(any(User.class));

    }

    @Test
    void getBehaviorsByUserAndTypeAndProject() {
        Mockito.when(behaviorRepository.getBehaviorsByUserAndTypeAndProject(any(User.class), ArgumentMatchers.eq(true), any(Project.class)))
                .thenReturn(Collections.singletonList(new Behavior(1L, true, null, null)));

        Mockito.when(behaviorRepository.getBehaviorsByUserAndTypeAndProject(any(User.class), ArgumentMatchers.eq(false), any(Project.class)))
                .thenReturn(Collections.singletonList(new Behavior(1L, false, null, null)));

        List<Behavior> likedResponse = behaviorService.getBehaviorsByUserAndTypeAndProject(new User(), true, new Project());
        List<Behavior> dislikedResponse = behaviorService.getBehaviorsByUserAndTypeAndProject(new User(), false, new Project());

        assertThat(likedResponse.get(0).isLiked()).isTrue();
        assertThat(likedResponse.size()).isEqualTo(1);
        assertThat(dislikedResponse.get(0).isLiked()).isFalse();
        assertThat(dislikedResponse.size()).isEqualTo(1);
    }

    @Test
    void getBehaviorsByUser() {
    }

}
