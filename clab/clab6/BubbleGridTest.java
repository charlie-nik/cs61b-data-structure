import org.junit.Test;
import static org.junit.Assert.*;

public class BubbleGridTest {

    @Test
    public void testBasic() {

        // test 01
        int[][] grid = {{1, 0, 0, 0},
                        {1, 1, 1, 0}};
        int[][] darts = {{1, 0}};
        int[] expected = {2};

        validate(grid, darts, expected);

        // test 02
        grid = new int[][]{{1, 1, 0},
                           {1, 0, 0},
                           {1, 1, 0},
                           {1, 1, 1}};
        darts = new int[][]{{2, 2}, {2, 0}};
        expected = new int[]{0, 4};

        validate(grid, darts, expected);
    }

    private void validate(int[][] grid, int[][] darts, int[] expected) {
        BubbleGrid sol = new BubbleGrid(grid);
        assertArrayEquals(expected, sol.popBubbles(darts));
    }
}
