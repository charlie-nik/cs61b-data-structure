package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QuadTreeTest {

    @Test
    public void testRandomRangeFinding() {
        int N = 10000;
        int M = 10000;
        List<Point> universe = TestTreeHelper.randomPoints(N);
        List<double[]> ranges = TestTreeHelper.randomRanges(M);

        NaivePointSet naive = new NaivePointSet(universe);
        QuadTree realDeal = new QuadTree(universe);

        for (double[] range : ranges) {
            List<Point> expected = naive.rangeFinding(range);
            List<Point> actual = realDeal.rangeFinding(range);

            assertEquals(expected.size(), actual.size());

            for (Point p : expected) {
                assertTrue(actual.contains(p));
            }
        }
    }

    @Test
    public void compareRangeFindingRuntime() {
        int N = 10_000_000;
        int M = 100;
        List<Point> universe = TestTreeHelper.randomPoints(N);
        List<double[]> ranges = TestTreeHelper.randomRanges(M);

        NaivePointSet naive = new NaivePointSet(universe);
        Stopwatch sw = new Stopwatch();
        for (double[] range : ranges) {
            naive.rangeFinding(range);
        }
        System.out.println("Naive point set: " + sw.elapsedTime() + " seconds.");

        KDTree kd = new KDTree(universe);
        sw = new Stopwatch();
        for (double[] range : ranges) {
            kd.rangeFinding(range);
        }
        System.out.println("K-d tree: " + sw.elapsedTime() + " seconds.");

        QuadTree qt = new QuadTree(universe);
        sw = new Stopwatch();
        for (double[] range : ranges) {
            qt.rangeFinding(range);
        }
        System.out.println("Quad tree: " + sw.elapsedTime() + " seconds.");

    }
}
