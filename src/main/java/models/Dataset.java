package models;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Dataset {

    private List<ColumnName> columns;
    private List<Map<String, Cell>> rows;

    public void seedProject(final Project project) {

        // Merge lists and distinct columnNames on name
        Map<String, ColumnName> headers = Stream.of(this.getColumns(), project.getColumnNames())
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toMap(ColumnName::getName, cn -> cn, (ColumnName x, ColumnName y) -> y));

        List<ColumnName> columns = new ArrayList<>(headers.values());
        Map<String, Cell> cells = new HashMap<>();

        project.setColumnNames(columns);
        columns.forEach((column) -> {
            column.setProject(project);
        });


        List<Map<String, Cell>> rows = this.getRows();
        if (rows != null) {
            List<Row> dbRows = project.getRows();

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

}
