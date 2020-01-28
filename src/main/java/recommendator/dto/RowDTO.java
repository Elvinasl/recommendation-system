package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recommendator.models.entities.Cell;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowDTO {
    private List<CellDTO> cells;
    private Integer reactions;

    public void convertCellsToDTO(List<Cell> cells) {
        this.cells = cells.stream()
                .map(cell -> {
                    CellDTO cellDTO = new CellDTO();
                    cellDTO.setColumnName(cell.getColumnName().getName());
                    cellDTO.setValue(cell.getValue());
                    return cellDTO;
                }).collect(Collectors.toList());
    }
}
