package recommendator.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import recommendator.dto.BehaviorDTO;
import recommendator.exceptions.responses.Response;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.User;
import recommendator.repositories.BehaviorRepository;
import static org.assertj.core.api.Assertions.assertThat;





import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class BehaviorServiceTest {

    @InjectMocks
    private BehaviorService behaviorService;

    @Mock
    private BehaviorRepository behaviorRepository;

    @Test
    void add() {

        List<Behavior> behaviors = Arrays.asList(new Behavior(), new Behavior());
        Mockito.when(behaviorRepository.getBehaviorsByUser(any(User.class))).thenReturn(behaviors);


        BehaviorDTO behaviorDTO = new BehaviorDTO(null, true, null);


//        assertThat(behaviorService.add("", behaviorDTO), new Response(""));
        verify(behaviorRepository, times(1)).add(any(Behavior.class));

    }

    @Test
    void getBehaviorsByUserAndTypeAndProject() {
    }

    @Test
    void getBehaviorsByUser() {
    }

}
