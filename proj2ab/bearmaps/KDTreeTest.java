package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KDTreeTest {

    @Test
    public void testRandomNearest() {
        int N = 10000;      // size of universe
        int M = 10000;      // number of tests
        List<Point> universe = randomPoints(N);
        List<Point> testPoints = randomPoints(M);

        NaivePointSet naive = new NaivePointSet(universe);
        KDTree realDeal = new KDTree(universe);

        Random r = new Random();
        for (Point p : testPoints) {
            assertEquals(naive.nearest(p.getX(), p.getY()), realDeal.nearest(p.getX(), p.getY()));
        }
    }

    @Test
    public void compareNearestRuntime() {
        int N = 100_000;
        int M = 10_000;
        List<Point> universe = randomPoints(N);
        List<Point> testPoints = randomPoints(M);

        Random r = new Random();

        NaivePointSet naive = new NaivePointSet(universe);
        Stopwatch sw = new Stopwatch();
        for (Point p : testPoints) {
            naive.nearest(p.getX(), p.getY());
        }
        System.out.println("Naive point set: " + sw.elapsedTime() + " seconds.");

        KDTree realDeal = new KDTree(universe);
        sw = new Stopwatch();
        for (Point p : testPoints) {
            realDeal.nearest(p.getX(), p.getY());
        }
        System.out.println("K-d tree: " + sw.elapsedTime() + " seconds.");
    }

    @Test
    public void testRandomRangeFinding() {
        int N = 10000;
        int M = 10000;
        List<Point> universe = randomPoints(N);
        List<double[]> ranges = randomRanges(M);

        NaivePointSet naive = new NaivePointSet(universe);
        KDTree realDeal = new KDTree(universe);

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
        int N = 100_000;
        int M = 10_000;
        List<Point> universe = randomPoints(N);
        List<double[]> ranges = randomRanges(M);

        NaivePointSet naive = new NaivePointSet(universe);
        Stopwatch sw = new Stopwatch();
        for (double[] range : ranges) {
            naive.rangeFinding(range);
        }
        System.out.println("Naive point set: " + sw.elapsedTime() + " seconds.");

        KDTree realDeal = new KDTree(universe);
        sw = new Stopwatch();
        for (double[] range : ranges) {
            realDeal.rangeFinding(range);
        }
        System.out.println("K-d tree: " + sw.elapsedTime() + " seconds.");
    }

    private static ArrayList<Point> randomPoints(int N) {
        Random r = new Random();
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            points.add(new Point(r.nextDouble(), r.nextDouble()));
        }
        return points;
    }

    private static ArrayList<double[]> randomRanges(int M) {
        Random r = new Random();
        ArrayList<double[]> ranges = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            double left = r.nextDouble();
            double right = r.nextDouble();
            while (right < left) right = r.nextDouble();

            double bottom = r.nextDouble();
            double top = r.nextDouble();
            while (top < bottom) top = r.nextDouble();

            ranges.add(new double[]{left, right, bottom, top});
        }
        return ranges;
    }
}
