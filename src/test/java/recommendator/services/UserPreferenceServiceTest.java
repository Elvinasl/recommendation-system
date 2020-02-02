package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.models.entities.*;
import recommendator.repositories.UserPreferenceRepository;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class UserPreferenceServiceTest {

    @Mock
    UserPreferenceRepository userPreferenceRepository;

    @InjectMocks
    UserPreferenceService userPreferenceService;

    @Test
    void createOrAdjust() {
        Row row = new Row();
        row.setId(1L);

        // Set two cells for the row, these are used to check if
        // the method's behavior is correct
        row.setCells(Arrays.asList(
                new Cell(1L, "a", null, row, null),
                new Cell(1L, "a", null, row, null)
        ));

        Cell cell = new Cell(1L, "a", null, row, null);
        UserPreference userPreference = new UserPreference(1L, new User(), cell);


        Mockito.when(userPreferenceRepository.getByCellOrNull(cell)).thenReturn(userPreference);
        Mockito.when(userPreferenceRepository.update(any(UserPreference.class)))
                .thenReturn(userPreference);

        userPreferenceService.createOrAdjust(new User(), row, new Behavior(1L, true, null, null));


        // checking whether the methods were called enough times
        verify(userPreferenceRepository, times(2)).getByCellOrNull(cell);
        verify(userPreferenceRepository, times(2)).update(userPreference);
    }
}
