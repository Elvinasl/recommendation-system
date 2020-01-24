package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import recommendator.repositories.ColumnNameRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ColumnNameServiceTest {

    @Mock
    ColumnNameRepository columnNameRepository;

    @InjectMocks
    ColumnNameService columnNameService;

    @Test
    void addOrUpdate() {

        // Initialize columnName
        ColumnName columnName = new ColumnName();

        // Set id to be sure it's every time the correct columnName
        columnName.setId(1L);

        // Initialize project
        Project project = new Project();

        // Mock some methods of the columnNameRepository
        Mockito.when(columnNameRepository.existsByNameAndProject(columnName.getName(), project))
                .thenReturn(true) // Returning true first call (test update)
                .thenReturn(false); // Returning false the second call (test add)
        Mockito.when(columnNameRepository.getByNameAndProject(columnName.getName(), project))
                .thenReturn(columnName); // This should return the columnName

        Mockito.when(columnNameRepository.update(columnName))
                .thenReturn(columnName); // This should return the columnName
        Mockito.when(columnNameRepository.add(columnName))
                .thenReturn(columnName); // This should return the columnName


        // Call the method
        ColumnName updated = columnNameService.addOrUpdate(columnName, project);

        // Check if it is returning the columnName
        assertThat(updated).isEqualTo(columnName);

        // Verify that it tries to update the columnName
        verify(columnNameRepository, times(1)).update(columnName);

        // Verify that it is not trying to add the columnName
        verify(columnNameRepository, times(0)).add(columnName);

        // Call the method again
        ColumnName added = columnNameService.addOrUpdate(columnName, project);

        // Check if it is returning the columnName
        assertThat(added).isEqualTo(columnName);

        // Verify that it tries to add the columnName
        verify(columnNameRepository, times(1)).add(columnName);

        // Verify that it is not trying to update the columnName
        verify(columnNameRepository, times(1)).update(columnName);

    }

    @Test
    void getCountForProject() {

        // Initialize project
        Project project = new Project();

        // Mock a method of the columnNameRepository
        Mockito.when(columnNameRepository.getCountForProject(project))
                .thenReturn(10L) // Returning 10 first call
                .thenReturn(20L); // Returning 20 the second call

        // Create variable
        long count;

        // Call count the first time
        count = columnNameService.getCountForProject(project);

        // Check count
        assertThat(count).isEqualTo(10L);

        // Call count the second time
        count = columnNameService.getCountForProject(project);

        // Check count
        assertThat(count).isEqualTo(20L);

    }
}