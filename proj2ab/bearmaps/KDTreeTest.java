package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {

    @Test
    public void testKDTree() {
        int N = 10000;
        Random r = new Random();

        NaivePointSet naive = new NaivePointSet(randomUniverse(N));
        KDTree realDeal = new KDTree(randomUniverse(N));
        for (int i = 0; i < N; i++) {
            double x = r.nextDouble();
            double y = r.nextDouble();
            assertEquals(naive.nearest(x, y), realDeal.nearest(x, y));
        }
    }

    @Test
    public void testRuntime() {
        int N = 100_000;
        int M = 10_000;
        Random r = new Random();

        NaivePointSet naive = new NaivePointSet(randomUniverse(N));
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < M; i++) {
            double x = r.nextDouble();
            double y = r.nextDouble();
            naive.nearest(x, y);
        }
        System.out.println("Naive point set: " + sw.elapsedTime() + " seconds.");

        KDTree realDeal = new KDTree(randomUniverse(N));
        sw = new Stopwatch();
        for (int i = 0; i < M; i++) {
            double x = r.nextDouble();
            double y = r.nextDouble();
            realDeal.nearest(x, y);
        }
        System.out.println("K-d tree: " + sw.elapsedTime() + " seconds.");
    }

    private static ArrayList<Point> randomUniverse(int N) {
        Random r = new Random(19971024);
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            points.add(new Point(r.nextDouble(), r.nextDouble()));
        }
        return points;
    }
}
