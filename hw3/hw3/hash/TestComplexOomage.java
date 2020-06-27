package hw3.hash;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* Create a list of Complex Oomages called deadlyList
     * that shows the flaw in the hashCode function.
     */

    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();

        // Your code here.
        int N = 10000;
        int p1 = StdRandom.uniform(0, 255);
        int p2 = StdRandom.uniform(0, 255);
        int p3 = StdRandom.uniform(0, 255);

        for (int i = 0; i < N; i++) {
            ArrayList<Integer> params = new ArrayList<>();
            int k = StdRandom.uniform(0, 10);
            for (int j = 0; j < k; j++) {
                params.add(StdRandom.uniform(0, 255));
            }
            // the last three powers
            params.add(p1);
            params.add(p2);
            params.add(p3);
            deadlyList.add(new ComplexOomage(params));
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
