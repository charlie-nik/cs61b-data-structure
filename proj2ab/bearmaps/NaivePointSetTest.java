package bearmaps;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NaivePointSetTest {

    @Test
    public void testNaiveNearest() {
        List<Point> universe = simpleUniverse();
        Point expected = universe.get(1);

        NaivePointSet nn = new NaivePointSet(universe);

        Point actual = nn.nearest(3.0, 4.0);
        assertEquals(expected, actual);
        assertEquals(3.3, actual.getX(), 0.01);
        assertEquals(4.4, actual.getY(), 0.01);
    }

    @Test
    public void testNaiveRangeFinding() {
        List<Point> universe = lectureUniverse2();
        List<Point> expected = List.of(universe.get(2), universe.get(5));

        NaivePointSet nn = new NaivePointSet(universe);
        List<Point> actual = nn.rangeFinding(new double[]{3, 5, 3.3, 5});

        assertEquals(expected.size(), actual.size());
        for (Point p : expected) {
            assertTrue(actual.contains(p));
        }
    }

    private static List<Point> simpleUniverse() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        return List.of(p1, p2, p3);
    }

    private static List<Point> lectureUniverse() {
        Point A = new Point(-1, -1);
        Point B = new Point(2, 2);
        Point C = new Point(0, 1);
        Point D = new Point(1, 0);
        Point E = new Point(-2, -2);

        return List.of(A, B, C, D, E);
    }

    private static List<Point> lectureUniverse2() {
        Point A = new Point(2, 3);
        Point B = new Point(4, 2);
        Point C = new Point(4, 5);
        Point D = new Point(3, 3);
        Point E = new Point(1, 5);
        Point F = new Point(4, 4);

        return List.of(A, B, C, D, E, F);
    }
}
