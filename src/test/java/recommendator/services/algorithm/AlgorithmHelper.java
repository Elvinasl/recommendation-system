package recommendator.services.algorithm;

import recommendator.models.containers.RowWithPoints;
import recommendator.models.entities.Cell;
import recommendator.models.entities.ColumnName;
import recommendator.models.entities.Row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlgorithmHelper {

    public static List<RowWithPoints> generateRowWithPointsList() {
        // mocking fake algorithm response
        List<RowWithPoints> rowWithPoints = new ArrayList<>();
        // we need to populate a list in a wrong order to verify whether the sorting algorithm is working
        rowWithPoints.add(prepareRow(20F, "medium"));
        rowWithPoints.add(prepareRow(10F, "smallest"));
        rowWithPoints.add(prepareRow(30F, "biggest"));
        return rowWithPoints;
    }

    // to get a row with points dto we need to create a column name in order to identify which row has what value
    public static RowWithPoints prepareRow(Float points, String value) {
        RowWithPoints biggestRow = new RowWithPoints(points);
        Cell cell = new Cell();
        cell.setValue(value);
        ColumnName columnName = new ColumnName();
        columnName.setName("a");
        cell.setColumnName(columnName);
        biggestRow.setCells(Collections.singletonList(cell));
        return biggestRow;
    }

    public static Row prepareRow(String cellValue, int numOfCells) {
        Row row = new Row();

        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < numOfCells; i++) {
            cells.add(prepareCell(cellValue));
        }
        row.setCells(cells);
        return row;
    }

    private static Cell prepareCell(String cellValue) {
        Cell cell = new Cell();
        cell.setValue(cellValue);
        ColumnName columnName = new ColumnName();
        columnName.setName("a");
        cell.setColumnName(columnName);
        return cell;
    }
}
