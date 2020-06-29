package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {

    @Test
    public void testKDTree() {
        int N = 10000;

        Random r = new Random();
        r.setSeed(19971024);

        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            points.add(new Point(r.nextDouble(), r.nextDouble()));
        }

        NaivePointSet naive = new NaivePointSet(points);
        KDTree realDeal = new KDTree(points);
        for (int i = 0; i < N; i++) {
            double x = r.nextDouble();
            double y = r.nextDouble();
            assertEquals(naive.nearest(x, y), realDeal.nearest(x, y));
        }
    }
}
