package bearmaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestTreeHelper {

    static List<Point> simpleUniverse() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        return List.of(p1, p2, p3);
    }

    static List<Point> lectureUniverse() {
        Point A = new Point(-1, -1);
        Point B = new Point(2, 2);
        Point C = new Point(0, 1);
        Point D = new Point(1, 0);
        Point E = new Point(-2, -2);

        return List.of(A, B, C, D, E);
    }

    static List<Point> lectureUniverse2() {
        Point A = new Point(2, 3);
        Point B = new Point(4, 2);
        Point C = new Point(4, 5);
        Point D = new Point(3, 3);
        Point E = new Point(1, 5);
        Point F = new Point(4, 4);

        return List.of(A, B, C, D, E, F);
    }

    static ArrayList<Point> randomPoints(int N) {
        Random r = new Random();
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            points.add(new Point(r.nextDouble(), r.nextDouble()));
        }
        return points;
    }

    static ArrayList<double[]> randomRanges(int M) {
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
