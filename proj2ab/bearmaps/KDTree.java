package bearmaps;

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
     * Should be in O(log N) time. */
    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        Point result = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        return nearest(root, target, result);
    }

    /***************************************************************************
     * Helper functions.
     ***************************************************************************/

    private Node growTree(Node n, Point p, boolean dimension) {
        if (n == null) return new Node(p, dimension);
        if (n.point.equals(p)) return n;

        int cmp = compare(p, n.point, dimension);
        if (cmp < 0) n.left = growTree(n.left, p, !dimension);
        else n.right = growTree(n.right, p, !dimension);

        return n;
    }

    private int compare(Point p1, Point p2, boolean dimension) {
        if (dimension) return Double.compare(p1.getX(), p2.getX());
        else return Double.compare(p1.getY(), p2.getY());
    }

    private Point limitPoint(Node n, Point target, boolean dimension) {
        if (dimension) return new Point(n.point.getX(), target.getY());
        else return new Point(target.getX(), n.point.getY());
    }

    private Point nearest(Node n, Point target, Point best) {
        if (n == null) return best;
        if (Point.distance(n.point, target) < Point.distance(best, target)) best = n.point;

        boolean leftFirst = compare(target, n.point, n.dimension) < 0;
        double limit = Point.distance(target, limitPoint(n, target, n.dimension));
        boolean badSide = limit < Point.distance(target, best);
        best = nearest(n, target, best, leftFirst, badSide);

        return best;
    }

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
}
