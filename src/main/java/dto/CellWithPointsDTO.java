package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Cell;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellWithPointsDTO extends Cell {

    private Float points = 0f;
}
