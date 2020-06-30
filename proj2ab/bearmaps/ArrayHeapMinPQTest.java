package bearmaps;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {

    @Test
    public void testFunctionality() {
        testBasics();
        testPriorityOrder();
        testChangePriority();
        testRandom();
    }

    @Test
    public void testBasics() {
        ArrayHeapMinPQ<Character> pq = new ArrayHeapMinPQ<>();

        assertEquals(0, pq.size());

        pq.add('a', 1);
        pq.add('b', 2);
        pq.add('c', 3);
        pq.add('d', 4);
        pq.add('e', 5);
        pq.add('f', 6);

        assertEquals(6, pq.size());
        assertTrue(pq.contains('a'));
        assertTrue(pq.contains('c'));
        assertTrue(pq.contains('e'));
        assertFalse(pq.contains('g'));
    }

    @Test
    public void testPriorityOrder() {
        ArrayHeapMinPQ<Character> pq = new ArrayHeapMinPQ<>();

        pq.add('d', 4);
        pq.add('a', 1);
        pq.add('b', 2);
        pq.add('f', 6);
        pq.add('c', 3);
        pq.add('e', 5);

        assertEquals('a', (char) pq.getSmallest());
        assertEquals('a', (char) pq.removeSmallest());
        assertEquals('b', (char) pq.removeSmallest());
        assertEquals('c', (char) pq.removeSmallest());
        assertEquals('d', (char) pq.getSmallest());

        assertEquals(3, pq.size());
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<Character> pq = new ArrayHeapMinPQ<>();

        pq.add('d', 4);
        pq.add('a', 1);
        pq.add('b', 2);
        pq.add('f', 6);
        pq.add('c', 3);
        pq.add('e', 5);

        pq.changePriority('a', 3);
        assertEquals('b', (char) pq.removeSmallest());
        assertNotEquals('d', (char) pq.removeSmallest());
        assertNotEquals('d', (char) pq.removeSmallest());
        assertEquals('d', (char) pq.removeSmallest());

        pq.add('a', 5);
        pq.changePriority('e', 4);
        assertEquals('e', (char) pq.removeSmallest());
        assertEquals('a', (char) pq.removeSmallest());
        assertEquals('f', (char) pq.removeSmallest());

        assertEquals(0, pq.size());
    }

    @Test
    public void testRandom() {
        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();

        MinPQ<Integer> priorities = new MinPQ<>();
        ArrayList<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            ints.add(i);
        }
        Collections.shuffle(ints);
        for (int i = 0; i < 300; i++) {
            priorities.insert(ints.get(i));
        }

        // test add() and size()
        for (int i = 0; i < 200; i++) {
            assertEquals(i, pq.size());
            pq.add(i, priorities.delMin());
        }

        // test getSmallest() and removeSmallest()
        for (int i = 0; i < 100; i++) {
            assertEquals(i, (int) pq.getSmallest());
            assertEquals(i, (int) pq.removeSmallest());
        }

        // test changePriority
        for (int i = 199; i >= 100; i--) {
            int p = priorities.delMin();
            pq.changePriority(i, p);
        }
        for (int i = 199; i >= 100; i--) {
            assertEquals(i, (int) pq.getSmallest());
            assertEquals(i, (int) pq.removeSmallest());
        }
    }

    @Test
    public void testRuntime() {
        int N = 100000;

        System.out.println("Testing runtime:");


        Stopwatch sw = new Stopwatch();
        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        for (int i = 0; i < N; i++) {
            pq.add(i, StdRandom.uniform(N));
        }
        System.out.println("add(): " + sw.elapsedTime() + " seconds.");


        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            pq.contains(StdRandom.uniform(N));
        }
        System.out.println("contains(): " + sw.elapsedTime() + " seconds.");


        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            pq.changePriority(i, StdRandom.uniform(N));
        }
        System.out.println("changePriority(): " + sw.elapsedTime() + " seconds.");


        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            pq.removeSmallest();
        }
        System.out.println("removeSmallest(): " + sw.elapsedTime() + " seconds.");


        System.out.println("End of test");

    }

    @Test
    public void testRemoveRuntime() {
        int N = 100_000;
        int M = 1;

        double totalTime = 0;
        for (int i = 0; i < M; i++) {
            ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
            for (int j = 0; j < N; j++) {
                pq.add(j, StdRandom.uniform(N));
            }
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < N; j++) {
                pq.removeSmallest();
            }
            totalTime += sw.elapsedTime();
        }

        System.out.println("Remove all items from a " + N + "-item heap: " + totalTime + " seconds.");

    }

    @Test
    public void compareRuntime() {
        int N = 20_000;

        // compare add()
        System.out.println("Comparing add() runtime:");
        Stopwatch sw = new Stopwatch();
        NaiveMinPQ<Integer> naivePQ = new NaiveMinPQ<>();
        for (int i = 0; i < N; i++) {
            naivePQ.add(i, StdRandom.uniform(N));
        }
        System.out.println("Naive PQ: " + sw.elapsedTime() + " seconds.");

        sw = new Stopwatch();
        ArrayHeapMinPQ<Integer> myPQ = new ArrayHeapMinPQ<>();
        for (int i = 0; i < N; i++) {
            myPQ.add(i, StdRandom.uniform(N));
        }
        System.out.println("My PQ: " + sw.elapsedTime() + " seconds.\n");


        // compare contains()
        System.out.println("Comparing contains() runtime:");
        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            naivePQ.contains(StdRandom.uniform(N));
        }
        System.out.println("Naive PQ: " + sw.elapsedTime() + " seconds.");

        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            myPQ.contains(StdRandom.uniform(N));
        }
        System.out.println("My PQ: " + sw.elapsedTime() + " seconds.\n");


        // compare changePriority()
        System.out.println("Comparing changePriority() runtime:");
        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            naivePQ.changePriority(StdRandom.uniform(N), StdRandom.uniform(N));
        }
        System.out.println("Naive PQ: " + sw.elapsedTime() + " seconds.");

        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            myPQ.changePriority(StdRandom.uniform(N), StdRandom.uniform(N));
        }
        System.out.println("My PQ: " + sw.elapsedTime() + " seconds.\n");


        // compare removeSmallest()
        System.out.println("Comparing removeSmallest() runtime:");
        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            naivePQ.removeSmallest();
        }
        System.out.println("Naive PQ: " + sw.elapsedTime() + " seconds.");

        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            myPQ.removeSmallest();
        }
        System.out.println("My PQ: " + sw.elapsedTime() + " seconds.\n");

    }
}
