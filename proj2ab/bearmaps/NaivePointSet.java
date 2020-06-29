package bearmaps;

import java.util.ArrayList;
import java.util.List;

public class NaivePointSet implements PointSet {

    private List<Point> universe;

    /* Constructor. */
    public NaivePointSet(List<Point> points) {
        this.universe = points;
    }

    /* Returns the closest point to the inputted coordinates. */
    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        Point result = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        for (Point p : universe) {
            if (Point.distance(p, target) < Point.distance(result, target)) {
                result = p;
            }
        }
        return result;
    }

    /* Returns a list of points within the given range. */
    @Override
    public List<Point> rangeFinding(double[] range) {
        List<Point> collected = new ArrayList<>();
        for (Point p : universe) {
            if (p.getX() >= range[0] && p.getX() <= range[1]
                    && p.getY() >= range[2] && p.getY() <= range[3]) {
                collected.add(p);
            }
        }
        return collected;
    }
}
