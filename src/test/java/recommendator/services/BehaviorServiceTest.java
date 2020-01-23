package recommendator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        Mockito.when(userService.findByExternalIdAndProjectOrNull(anyString(), any(Project.class))).thenReturn(new User()).thenReturn(null);

        Response response = behaviorService.add("test", new BehaviorDTO(new ArrayList<>(),false, "userId"));
        assertThat("Behavior recorded").isEqualTo(response.getMessage());
        verify(behaviorRepository, times(1)).add(any(Behavior.class));

        // Checking if the user is null
        response = behaviorService.add("test", new BehaviorDTO(new ArrayList<>(),false, "userId"));
        assertThat("Behavior recorded").isEqualTo(response.getMessage());
        verify(userService, times(1)).add(any(User.class));

    }

    @Test
    void getBehaviorsByUserAndTypeAndProject() {
    }

    @Test
    void getBehaviorsByUser() {
    }

}
