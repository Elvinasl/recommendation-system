package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.entities.Cell;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellWithPointsDTO extends Cell {

    private Float points = 0f;

    public CellWithPointsDTO(Cell cell) {
        super(cell.getId(), cell.getValue(), cell.getColumnName(), cell.getRow(), cell.getUserPreference());
    }

}
