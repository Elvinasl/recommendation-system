package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class Dataset {

    private List<ColumnName> columns;
    private List<Map<String, Cell>> rows;

    public void seedProject(Project project) {

        List<ColumnName> columns = this.getColumns();
        Map<String, ColumnName> headers;
        Map<String, Cell> cells = new HashMap<>();
        project.setColumnNames(columns);
        columns.forEach((column) -> {
            column.setProject(project);
        });

        headers = columns.stream().collect(Collectors.toMap(ColumnName::getName, Function.identity()));

        List<Map<String, Cell>> rows = this.getRows();
        List<Row> dbRows = new ArrayList<>();
        rows.forEach((row) -> {
            Row dbRow = new Row();
            dbRow.setProject(project);
            row.forEach((columnName, cell) -> {
                ColumnName col = headers.get(columnName);
                if (cells.containsKey(cell.getValue())) {
                    cell = cells.get(cell.getValue());
                } else {
                    cells.put(cell.getValue(), cell);
                    cell.setColumnName(col);
                    col.add(cell);
                }
                cell.addRow(dbRow);
                dbRow.addCell(cell);
            });
            dbRows.add(dbRow);
        });

        project.setRows(dbRows);
    }

}
