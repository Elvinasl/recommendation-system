package services.algorithm;

import dto.CellWithPointsDTO;
import dto.RowWithPointsDTO;
import models.Behavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.BehaviorService;
import services.ColumnNameService;

import java.util.*;

@Service
public class CellPointsFilter implements AlgorithmFilter {

    private final ColumnNameService columnNameService;
    private BehaviorService behaviorService;

    @Autowired
    public CellPointsFilter(BehaviorService behaviorService, ColumnNameService columnNameService) {

        this.behaviorService = behaviorService;
        this.columnNameService = columnNameService;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {

        // Get the list of behaviors
        List<Behavior> behaviors = filtersData.getBehaviors();
        if (behaviors == null) {
            // Behaviors aren't set in another filter, so get them for the user
            behaviors = behaviorService.getBehaviorsByUser(filtersData.getUser());

            // User don't have any behaviors, so set an empty list
            if (behaviors == null) behaviors = new ArrayList<>();

            // Add it to the behaviors list
            filtersData.setBehaviors(behaviors);
        }

        // Get number of columns for the project to calculate with
        long nrOfColumns = columnNameService.getCountForProject(filtersData.getProject());

        // Create a list with unique cells which have a behavior
        Map<String, CellWithPointsDTO> uniqueCellsFromBehaviors = new HashMap<>();
        behaviors.forEach(behavior -> {
            behavior.getRow().getCells().forEach(cell -> {


                CellWithPointsDTO cellWithPoints;
                if (uniqueCellsFromBehaviors.containsKey(cell.getValue())) {
                    // Get the cell with points from the list
                    cellWithPoints = uniqueCellsFromBehaviors.get(cell.getValue());
                } else {
                    // Cell doesn't exist in the list, so creating it here
                    cellWithPoints = new CellWithPointsDTO();
                    cellWithPoints.setId(cell.getId());
                    cellWithPoints.setColumnName(cell.getColumnName());
                    cellWithPoints.setRow(cell.getRow());
                    cellWithPoints.setValue(cell.getValue());
                    cellWithPoints.setUserPreference(cell.getUserPreference());
                    cellWithPoints.setWeight(cell.getWeight());

                    // Add it to the list
                    uniqueCellsFromBehaviors.put(cell.getValue(), cellWithPoints);
                }
                // Get the points
                Float points = cellWithPoints.getPoints();

                // Calculate the points
                points += (
                        behavior.isLiked() ? 1 : -1 // Make it positive or negative based on like
                ) * (
                        cellWithPoints.getWeight() *                  // The points added are based on
                                cellWithPoints.getColumnName().getWeight() /  // cell weight and column weight.
                                (
                                        nrOfColumns *     // number of columns, because the points are for a cell
                                                20f   // 20f because we divide want the average of cell-weight and column-
                                )       // weight and we don't want to have an overflow exception.
                );

                // Set the points to the cell
                cellWithPoints.setPoints(points);
            });
        });


        filtersData.getRows().forEach(rowWithPoints -> {
            rowWithPoints.getCells().forEach(cell -> {
                if (uniqueCellsFromBehaviors.containsKey(cell.getValue())) {
                    // Cell is (dis)liked so add points to the row points
                    rowWithPoints.setPoints(rowWithPoints.getPoints() + uniqueCellsFromBehaviors.get(cell.getValue()).getPoints());
                }
            });
        });


        // Sort list by points
        Comparator<RowWithPointsDTO> compareRowsByPoints = Comparator.comparing(RowWithPointsDTO::getPoints).reversed();
        Collections.sort(filtersData.getRows(), compareRowsByPoints);

        return filtersData;
    }
}
