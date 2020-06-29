package bearmaps;

import java.util.List;

public class KDTree {

    // helper class
    private class Node {

        private final Point point;
        private final int dimension;
        private Node left;
        private Node right;

        Node(Point point, int dimension) {
            this.point = point;
            this.dimension = dimension;
        }
    }

    private Node root;

    /* Constructor. */
    public KDTree(List<Point> points) {
        for (Point p : points) {
            root = growTree(root, p, 0); // start at 0th-dimension
        }
    }

    /* Returns the closest point to the inputted coordinates.
     * It should be in O(log N) time. */
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        Point result = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        return nearest(root, target, result);
    }

    /***************************************************************************
     * Helper functions.
     ***************************************************************************/

    private Node growTree(Node n, Point p, int dimension) {
        if (n == null) return new Node(p, dimension);
        if (n.point.equals(p)) return n;

        if (dimension == 0) {
            if (p.getX() < n.point.getX()) n.left = growTree(n.left, p, 1);
            else n.right = growTree(n.right, p, 1);
        } else {
            if (p.getY() < n.point.getY()) n.left = growTree(n.left, p, 0);
            else n.right = growTree(n.right, p, 0);
        }

        return n;
    }

    private Point nearest(Node n, Point target, Point result) {
        if (n == null) return result;
        if (Point.distance(n.point, target) < Point.distance(result, target)) result = n.point;

        boolean leftFirst;
        double limit;
        if (n.dimension == 0) {
            leftFirst = target.getX() < n.point.getX();
            limit = Point.distance(target, new Point(n.point.getX(), target.getY()));
        } else {
            leftFirst = target.getY() < n.point.getY();
            limit = Point.distance(target, new Point(target.getX(), n.point.getY()));
        }
        boolean badSide = limit < Point.distance(target, result);
        result = nearest(n, target, result, leftFirst, badSide);
        return result;
    }

    private Point nearest(Node n, Point target, Point result, boolean leftFirst, boolean badSide) {
        if (leftFirst) {
            // good side
            result = nearest(n.left, target, result);
            // bad side
            if (badSide) result = nearest(n.right, target, result);
        } else {
            // good side
            result = nearest(n.right, target, result);
            // bad side
            if (badSide) result = nearest(n.left, target, result);
        }
        return result;
    }
}
