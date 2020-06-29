package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {

    private List<Point> points;

    /* Constructor. */
    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    /* Returns the closest point to the inputted coordinates. */
    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        Point result = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        for (Point p : points) {
            if (Point.distance(p, target) < Point.distance(result, target)) {
                result = p;
            }
        }
        return result;
    }
}
