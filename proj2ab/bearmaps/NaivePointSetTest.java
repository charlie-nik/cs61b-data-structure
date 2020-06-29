package bearmaps;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NaivePointSetTest {

    @Test
    public void testNaiveNearest() {
        List<Point> universe = TestTreeHelper.simpleUniverse();
        Point expected = universe.get(1);

        NaivePointSet nn = new NaivePointSet(universe);

        Point actual = nn.nearest(3.0, 4.0);
        assertEquals(expected, actual);
        assertEquals(3.3, actual.getX(), 0.01);
        assertEquals(4.4, actual.getY(), 0.01);
    }

    @Test
    public void testNaiveRangeFinding() {
        List<Point> universe = TestTreeHelper.lectureUniverse2();
        List<Point> expected = List.of(universe.get(2), universe.get(5));

        NaivePointSet nn = new NaivePointSet(universe);
        double[] range = new double[]{3, 5, 3.3, 5};
        List<Point> actual = nn.rangeFinding(range);

        assertEquals(expected.size(), actual.size());
        for (Point p : expected) {
            assertTrue(actual.contains(p));
        }
    }

}
