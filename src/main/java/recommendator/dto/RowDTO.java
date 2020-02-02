package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.Cell;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This DTO holds information about a single {@link recommendator.models.entities.Row}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowDTO {
    private Long id;
    private List<CellDTO> cells;
    private Integer reactions;

    /**
     * Converts a list of {@link Cell} into a list of {@link CellDTO}
     *
     * @param cells to convert
     */
    public void convertCellsToDTO(List<Cell> cells) {
        this.cells = cells.stream()
                .map(cell -> {
                    CellDTO cellDTO = new CellDTO();
                    cellDTO.setId(cell.getId());
                    cellDTO.setColumnName(cell.getColumnName().getName());
                    cellDTO.setValue(cell.getValue());
                    return cellDTO;
                }).collect(Collectors.toList());
    }
}
