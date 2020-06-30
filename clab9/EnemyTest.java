import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /* TEST ENEMY CLASS. */

    @Test
    public void printEnemies() throws FileNotFoundException {
        Enemy e = new Enemy(party3(3).g, 2);
        printEnemies(e);
    }

    @Test
    public void printTables() throws FileNotFoundException {
        Enemy e = new Enemy(party1(2).g, 2);
        printTables(e);
    }

    @Test
    public void printAll() throws FileNotFoundException {
        Enemy e = new Enemy(basic2(2).g, 2);
        printEnemies(e);
        System.out.println();
        printTables(e);
        System.out.println();
        System.out.println("Separable: " + e.isSeparable);
    }

    private void printEnemies(Enemy e) {
        Map<String, Set<String>> enemyOf = e.getEnemyOf();
        System.out.println("Enemy list:");
        for (String p : enemyOf.keySet()) {
            System.out.print(p + ": ");
            for (String s : enemyOf.get(p)) {
                System.out.print(s + ", ");
            }
            System.out.println();
        }
    }

    private void printTables(Enemy e) {
        List<Set<String>> forbidden = e.getForbidden();
        System.out.println("Tables:");
        for (int i = 0; i < forbidden.size(); i++) {
            System.out.print("Table " + (i + 1) + " forbidden members: ");
            for (String s : forbidden.get(i)) {
                System.out.print(s + ", ");
            }
            System.out.println();
        }
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
