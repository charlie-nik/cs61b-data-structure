import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import java.io.FileNotFoundException;

public class EnemyTest {

    @Test
    public void twoGroups() throws FileNotFoundException {
        int N = 2;
        assertFalse(basic1(N).isSeparable());
        assertTrue(basic2(N).isSeparable());
        assertFalse(basic3(N).isSeparable());
        assertTrue(party1(N).isSeparable());
        assertTrue(party2(N).isSeparable());
        assertFalse(party3(N).isSeparable());
        assertFalse(party4(N).isSeparable());
    }

    @Test
    public void threeGroups() throws FileNotFoundException {
        int N = 3;
        assertTrue(basic1(N).isSeparable());
        assertTrue(basic2(N).isSeparable());
        assertTrue(basic3(N).isSeparable());
        assertTrue(party1(N).isSeparable());
        assertTrue(party2(N).isSeparable());
        assertTrue(party3(N).isSeparable());
        assertTrue(party4(N).isSeparable());
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
