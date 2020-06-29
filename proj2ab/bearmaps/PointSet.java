package bearmaps;

import java.util.List;

public interface PointSet {
    Point nearest(double x, double y);
    List<Point> rangeFinding(double[] rect);
}
