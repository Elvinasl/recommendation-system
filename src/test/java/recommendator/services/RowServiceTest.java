package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.dto.CellDTO;
import recommendator.dto.DatasetCellDTO;
import recommendator.exceptions.NotFoundException;
import recommendator.exceptions.RowAlreadyExistsException;
import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Project;
import recommendator.models.entities.Row;
import recommendator.models.entities.User;
import recommendator.repositories.ColumnNameRepository;
import recommendator.repositories.RowRepository;

import javax.persistence.NoResultException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RowServiceTest {

    @Mock
    private RowRepository rowRepository;

    @Mock
    private ColumnNameRepository columnNameRepository;

    @InjectMocks
    RowService rowService;

    @Test
    void addOrUpdate() {

        List<DatasetCellDTO> datasetRow = Arrays.asList(new DatasetCellDTO(), new DatasetCellDTO());
        datasetRow.get(0).setColumnName("column");
        datasetRow.get(1).setColumnName("column");

        Project project = new Project();

        Mockito.when(columnNameRepository.getByNameAndProject("column", project))
                .thenThrow(NoResultException.class) // Throw an NoResultException first time
                .thenReturn(new ColumnName()); // Return a ColumnName object the second time

        Mockito.when(rowRepository.rowExists(any(Project.class), anyList()))
                .thenReturn(true) // Return first time that the row exists
                .thenReturn(false); // Return second time that the row not exists


        // Check if the exception is correct
        assertThatThrownBy(() -> rowService.addOrUpdate(datasetRow, project))
                .hasMessage("Unknown column with name: column")
                .isExactlyInstanceOf(NotFoundException.class);

        assertThatThrownBy(() -> rowService.addOrUpdate(datasetRow, project))
                .hasMessage("Row duplicate found for row: null, null")
                .isExactlyInstanceOf(RowAlreadyExistsException.class);

        // Call the method for the third time
        rowService.addOrUpdate(datasetRow, project);

        // Verify if the add method was called.
        verify(rowRepository, times(1)).add(any(Row.class));

    }

    @Test
    void getRowByCellDTOAndProject() {

        List<CellDTO> cellDTOList = Arrays.asList(new CellDTO(), new CellDTO());
        List<String> cellValues = Arrays.asList(null, null);

        Project project = new Project();
        Row row = new Row();

        // Mock method
        Mockito.when(rowRepository.findRowByCellsAndProject(cellValues, project))
                .thenReturn(row);

        // Call method to test
        Row response = rowService.getRowByCellDTOAndProject(cellDTOList, project);


        // Verify id the method is called
        verify(rowRepository).findRowByCellsAndProject(cellValues, project);

        // Be sure the row from the repository is called
        assertThat(response).isEqualTo(row);
    }

    @Test
    void getMostLikedContentForProjectAndUser() {

        Project project = new Project();
        User user = new User();

        List<RowWithPoints> rowWithPointsList = Arrays.asList(new RowWithPoints(), new RowWithPoints());

        // Mock method
        Mockito.when(rowRepository.getMostLikedContentForProjectAndUser(project, user))
                .thenReturn(rowWithPointsList); // Return a row

        // Call method to test
        List<RowWithPoints> response = rowService.getMostLikedContentForProjectAndUser(project, user);

        // Check for the right response
        assertThat(response).isEqualTo(rowWithPointsList);
    }
}
