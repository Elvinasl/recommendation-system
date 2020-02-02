package recommendator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import recommendator.models.entities.Cell;
import recommendator.models.entities.Row;
import recommendator.repositories.CellRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CellServiceTest {

    @Mock
    private CellRepository cellRepository;

    @InjectMocks
    private CellService cellService;

    @Test
    void getByRow() {

        Row r = new Row();
        r.setId(1L);

        // Set two cells for the row, these are used to check if
        // the method's behavior is correct
        r.setCells(Arrays.asList(
                new Cell(1L, "a", null, r, null),
                new Cell(2L, "b", null, r, null)
        ));

        // Mock the repository method
        Mockito.when(cellRepository.getByRow(r)).thenReturn(r.getCells());

        // Get the response from the cellService
        List<Cell> response = cellService.getByRow(r);

        // Check if the sizes of the cells are equal
        assertThat(response.size()).isEqualTo(2);

        // Check if first cell is the same as the first one in row
        assertThat(response.get(0)).isEqualTo(r.getCells().get(0));
    }
}
