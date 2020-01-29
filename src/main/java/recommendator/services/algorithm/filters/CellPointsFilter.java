package recommendator.services.algorithm.filters;

import recommendator.models.containers.CellWithPoints;
import recommendator.models.entities.Behavior;
import recommendator.models.entities.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendator.services.ColumnNameService;
import recommendator.services.algorithm.FiltersData;
import recommendator.services.BehaviorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CellPointsFilter class is changing the rowList by the cell points
 * For example:
 * If you like comedy movies a lot, than that cell gets more points and
 * rows which having that cell value are getting those points too so they
 * climb in the list of rows and give more relevant rows
 */
@Service
public class CellPointsFilter implements AlgorithmFilter {

    private ColumnNameService columnNameService;
    private BehaviorService behaviorService;

    private Map<String, CellWithPoints> uniqueCellsFromBehaviors;
    private List<Behavior> behaviors;


    @Autowired
    public CellPointsFilter(BehaviorService behaviorService, ColumnNameService columnNameService) {

        this.behaviorService = behaviorService;
        this.columnNameService = columnNameService;
    }

    @Override
    public FiltersData filter(FiltersData filtersData) {

        // Set the behaviors
        this.setBehaviors(filtersData);

        // Get number of columns for the project to calculate with
        long nrOfColumns = columnNameService.getCountForProject(filtersData.getProject());

        // Set the unique cells with points from the behaviors
        this.setUniqueCellsFromBehaviors(nrOfColumns);

        // Modify row points by the cell points
        this.modifyRowPointsByCellPoints(filtersData);

        return filtersData;
    }

    /**
     * Getting the {@link Behavior}'s from the filteredData or get it from
     * the database and set it into filtered containers.
     *
     * @param filtersData from the previous filter
     */
    private void setBehaviors(FiltersData filtersData) {
        // Get the list of behaviors
        this.behaviors = filtersData.getBehaviors();
        if (this.behaviors == null) {
            // Behaviors aren't set in another filter, so get them for the user
            this.behaviors = behaviorService.getBehaviorsByUser(filtersData.getUser());

            // User don't have any behaviors, so set an empty list
            if (this.behaviors == null) this.behaviors = new ArrayList<>();

            // Add it to the behaviors list
            filtersData.setBehaviors(this.behaviors);
        }
    }

    /**
     * This method set unique cells from the behaviors with their calculated points.
     *
     * @param nrOfColumns Number of columns for the project
     */
    private void setUniqueCellsFromBehaviors(float nrOfColumns) {
        // Create a list with unique cells which have a behavior
        this.uniqueCellsFromBehaviors = new HashMap<>();
        this.behaviors.forEach(behavior -> {
            behavior.getRow().getCells().forEach(cell -> {


                CellWithPoints cellWithPoints = this.getOrAddUniqueCellWithPoints(cell);

                // Get the points
                Float points = cellWithPoints.getPoints();

                // Calculate the points
                points += (
                        behavior.isLiked() ? 1 : -1 // Make it positive or negative based on like
                ) * (
                        cellWithPoints.getWeight() *                 // The points added are based on
                                cellWithPoints.getColumnName().getWeight() / // cell weight and column weight.
                                (
                                        nrOfColumns * // number of columns, because the points are for a cell
                                                20f // 20f because we divide want the average of cell-weight and column-
                                )       // weight and we don't want to have an overflow exception.
                );

                // Set the points to the cell
                cellWithPoints.setPoints(points);
            });
        });
    }

    /**
     * Get cellWithPoints from the map of unique cells by cell or convert and add it.
     *
     * @param cell Cell to get/convert
     * @return CellWithPoints to get a cell with points to calculate with
     */
    private CellWithPoints getOrAddUniqueCellWithPoints(Cell cell) {
        if (this.uniqueCellsFromBehaviors.containsKey(cell.getValue())) {
            // Get the cell with points from the list
            return this.uniqueCellsFromBehaviors.get(cell.getValue());
        } else {
            // Cell doesn't exist in the list, so create
            CellWithPoints cellWithPoints = new CellWithPoints(cell);

            // add it to the list
            this.uniqueCellsFromBehaviors.put(cell.getValue(), cellWithPoints);

            // Return cell
            return cellWithPoints;
        }
    }


    /**
     * Modify the row points by the cell points
     *
     * @param filtersData
     */
    private void modifyRowPointsByCellPoints(FiltersData filtersData) {
        if(filtersData.getRows() == null) {
            filtersData.setRows(new ArrayList<>());
            return;
        }

        filtersData.getRows().forEach(rowWithPoints -> {
            rowWithPoints.getCells().forEach(cell -> {
                if (this.uniqueCellsFromBehaviors.containsKey(cell.getValue())) {
                    // Cell is (dis)liked so add points to the row points
                    rowWithPoints.setPoints(rowWithPoints.getPoints() + this.uniqueCellsFromBehaviors.get(cell.getValue()).getPoints());
                }
            });
        });
    }
}
