package recommendator.services.algorithm;

import recommendator.models.containers.RowWithPoints;

import java.util.Comparator;

public class AlgorithmPointsComparator {
    public static Comparator<RowWithPoints> comparator = Comparator.comparing(RowWithPoints::getPoints).reversed();
}
