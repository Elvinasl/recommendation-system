package recommendator.services.algorithm;

import recommendator.models.containers.RowWithPoints;

import java.util.Comparator;

/**
 * This class contains a comparator to sort a list of {@link RowWithPoints} by points.
 * The sorting is from high to low.
 */
public class AlgorithmPointsComparator {
    public static Comparator<RowWithPoints> comparator = Comparator.comparing(RowWithPoints::getPoints).reversed();
}
