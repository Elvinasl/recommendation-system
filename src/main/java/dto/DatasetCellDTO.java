package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatasetCellDTO extends CellDTO {

    @Min(1)
    @Max(100)
    @NotNull
    private int weight = 50;

    public static DatasetCellDTO create(int weight, String columnName, String value) {
        DatasetCellDTO cell = new DatasetCellDTO(weight);
        cell.setColumnName(columnName);
        cell.setValue(value);
        return cell;
    }
}
