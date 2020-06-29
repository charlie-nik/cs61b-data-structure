package bearmaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuadTree implements PointSet {

    private static class Node {

        private final Point point;
        private Node SOUTH_WEST, SOUTH_EAST, NORTH_WEST, NORTH_EAST;

        Node(Point point) {
            this.point = point;
        }
    }

    private Node root;

    public QuadTree(List<Point> points) {
        Collections.shuffle(points);
        for (Point p : points) {
            root = growTree(root, p);
        }
    }

    @Override
    public Point nearest(double x, double y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Point> rangeFinding(double[] range) {
        List<Point> collected = new ArrayList<>();
        rangeFinding(root, collected, range);
        return collected;
    }

    /***************************************************************************
     * Helper functions for construction.
     ***************************************************************************/

    private Node growTree(Node n, Point p) {
        if (n == null) return new Node(p);
        if (n.point.equals(p)) return n;

        if (p.getX() >= n.point.getX()) {   // east
            if (p.getY() >= n.point.getY()) n.NORTH_EAST = growTree(n.NORTH_EAST, p);
            else n.SOUTH_EAST = growTree(n.SOUTH_EAST, p);
        } else {    // west
            if (p.getY() >= n.point.getY()) n.NORTH_WEST = growTree(n.NORTH_WEST, p);
            else n.SOUTH_WEST = growTree(n.SOUTH_WEST, p);
        }

        return n;
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

        if (range[0] <= n.point.getX()) {   // west?
            if (range[2] <= n.point.getY()) rangeFinding(n.SOUTH_WEST, collected, range);
            if (range[3] >= n.point.getY()) rangeFinding(n.NORTH_WEST, collected, range);
        }
        if (range[1] >= n.point.getX()) {    // east?
            if (range[2] <= n.point.getY()) rangeFinding(n.SOUTH_EAST, collected, range);
            if (range[3] >= n.point.getY()) rangeFinding(n.NORTH_EAST, collected, range);
        }
    }
}
