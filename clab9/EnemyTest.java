import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.io.FileNotFoundException;

public class EnemyTest {

    @Test
    public void DFS() throws FileNotFoundException {
        twoGroupsDFS();
        threeGroupsDFS();
    }

    @Test
    public void BFS() throws FileNotFoundException {
        twoGroupsBFS();
        threeGroupsBFS();
    }

    @Test
    public void compareDFSandBFS() throws FileNotFoundException {
        int N = 100;

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            DFS();
        }
        System.out.println("DFS: " + sw.elapsedTime() + " seconds.");

        sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            BFS();
        }
        System.out.println("BFS: " + sw.elapsedTime() + " seconds.");
    }

    @Test
    public void twoGroupsDFS() throws FileNotFoundException {
        int N = 2;
        assertFalse(basic1(N).isSeparableDFS());
        assertTrue(basic2(N).isSeparableDFS());
        assertFalse(basic3(N).isSeparableDFS());
        assertTrue(party1(N).isSeparableDFS());
        assertTrue(party2(N).isSeparableDFS());
        assertFalse(party3(N).isSeparableDFS());
        assertFalse(party4(N).isSeparableDFS());
    }

    @Test
    public void threeGroupsDFS() throws FileNotFoundException {
        int N = 3;
        assertTrue(basic1(N).isSeparableDFS());
        assertTrue(basic2(N).isSeparableDFS());
        assertTrue(basic3(N).isSeparableDFS());
        assertTrue(party1(N).isSeparableDFS());
        assertTrue(party2(N).isSeparableDFS());
        assertTrue(party3(N).isSeparableDFS());
        assertTrue(party4(N).isSeparableDFS());
    }

    @Test
    public void twoGroupsBFS() throws FileNotFoundException {
        int N = 2;
        assertFalse(basic1(N).isSeparableDFS());
        assertTrue(basic2(N).isSeparableDFS());
        assertFalse(basic3(N).isSeparableDFS());
        assertTrue(party1(N).isSeparableDFS());
        assertTrue(party2(N).isSeparableDFS());
        assertFalse(party3(N).isSeparableDFS());
        assertFalse(party4(N).isSeparableDFS());
    }

    @Test
    public void threeGroupsBFS() throws FileNotFoundException {
        int N = 3;
        assertTrue(basic1(N).isSeparableDFS());
        assertTrue(basic2(N).isSeparableDFS());
        assertTrue(basic3(N).isSeparableDFS());
        assertTrue(party1(N).isSeparableDFS());
        assertTrue(party2(N).isSeparableDFS());
        assertTrue(party3(N).isSeparableDFS());
        assertTrue(party4(N).isSeparableDFS());
    }

    /* TEST UTILITY. */

    private static SeparableEnemySolver basic1(int N) throws FileNotFoundException {
        return new SeparableEnemySolver("input/basic1", N);
    }

    private static SeparableEnemySolver basic2(int N) throws FileNotFoundException {
        return new SeparableEnemySolver("input/basic2", N);
    }

    private static SeparableEnemySolver basic3(int N) throws FileNotFoundException {
        return new SeparableEnemySolver("input/basic3", N);
    }

    private static SeparableEnemySolver party1(int N) throws FileNotFoundException {
        return new SeparableEnemySolver("input/party1", N);
    }

    private static SeparableEnemySolver party2(int N) throws FileNotFoundException {
        return new SeparableEnemySolver("input/party2", N);
    }

    private static SeparableEnemySolver party3(int N) throws FileNotFoundException {
        return new SeparableEnemySolver("input/party3", N);
    }

    private static SeparableEnemySolver party4(int N) throws FileNotFoundException {
        return new SeparableEnemySolver("input/party4", N);
    }
}
