package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KDTree implements PointSet {

    // helper class
    private static class Node {
        private final Point point;
        private final boolean dimension;
        private Node left;
        private Node right;

        Node(Point point, boolean dimension) {
            this.point = point;
            this.dimension = dimension;
        }
    }

    private static final boolean HORIZONTAL = true;
    private Node root;

    /* Constructor. */
    public KDTree(List<Point> points) {
        Collections.shuffle(points);
        for (Point p : points) {
            root = growTree(root, p, HORIZONTAL); // start at 0th-dimension
        }
    }

    /* Returns the closest point to the inputted coordinates.
     * Should take O(log N) time. */
    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        Point result = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        return nearest(root, target, result);
    }

    /* Returns a list of points within the given range.
     * Should take O(log N) time. */
    public List<Point> rangeFinding(double[] range) {
        List<Point> collected = new ArrayList<>();
        rangeFinding(root, collected, range);
        return collected;
    }

    /***************************************************************************
     * Helper functions for construction.
     ***************************************************************************/

    private Node growTree(Node n, Point p, boolean dimension) {
        if (n == null) return new Node(p, dimension);
        if (n.point.equals(p)) return n;

        int cmp = compare(p, n.point, dimension);
        if (cmp < 0) n.left = growTree(n.left, p, !dimension);
        else n.right = growTree(n.right, p, !dimension);

        return n;
    }

    /***************************************************************************
     * Helper functions for nearest().
     ***************************************************************************/

    private int compare(Point p1, Point p2, boolean dimension) {
        if (dimension) return Double.compare(p1.getX(), p2.getX());
        else return Double.compare(p1.getY(), p2.getY());
    }

    private double limit(Node n, Point target, boolean dimension) {
        Point limitPoint;
        if (dimension) limitPoint = new Point(n.point.getX(), target.getY());
        else limitPoint = new Point(target.getX(), n.point.getY());
        return Point.distance(target, limitPoint);
    }

    private Point nearest(Node n, Point target, Point best) {
        if (n == null) return best;
        if (Point.distance(n.point, target) < Point.distance(best, target)) best = n.point;

        boolean leftFirst = compare(target, n.point, n.dimension) < 0;
        boolean badSide = limit(n, target, n.dimension) < Point.distance(target, best);
        best = nearest(n, target, best, leftFirst, badSide);

        return best;
    }

    // this is Depth First Search (dfs) ?
    private Point nearest(Node n, Point target, Point result, boolean leftFirst, boolean badSide) {
        if (leftFirst) {
            result = nearest(n.left, target, result);
            if (badSide) result = nearest(n.right, target, result);
        } else {
            result = nearest(n.right, target, result);
            if (badSide) result = nearest(n.left, target, result);
        }
        return result;
    }

    /***************************************************************************
     * Helper functions for rangeFinding().
     ***************************************************************************/

    private boolean withinRange(Point p, double[] range) {
        return p.getX() >= range[0] && p.getX() <= range[1]
                && p.getY() >= range[2] && p.getY() <= range[3];
    }

    private void rangeFinding(Node n, List<Point> collected, double[] range) {
        if (n == null) return;
        if (withinRange(n.point, range)) collected.add(n.point);

        if (n.dimension) {
            if (range[0] <= n.point.getX()) rangeFinding(n.left, collected, range);
            if (range[1] >= n.point.getX()) rangeFinding(n.right, collected, range);
        } else {
            if (range[2] <= n.point.getY()) rangeFinding(n.left, collected, range);
            if (range[3] >= n.point.getY()) rangeFinding(n.right, collected, range);
        }
    }
}
