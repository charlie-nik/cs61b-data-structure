import org.junit.Test;
import static org.junit.Assert.*;

public class TestBubbleGrid {

    @Test
    public void testIsBubble() {
        BubbleGrid bg = new BubbleGrid(new int[][]{{1, 1, 0},
                                                   {1, 0, 0},
                                                   {1, 1, 0},
                                                   {1, 1, 1}});
        assertTrue(bg.isBubble(0, 0));
        assertTrue(bg.isBubble(2, 1));
        assertFalse(bg.isBubble(1, 2));
    }

    @Test
    public void testSizeOf() {
        BubbleGrid bg = new BubbleGrid(new int[][]{{1, 1, 0},
                                                    {1, 0, 0},
                                                    {1, 1, 0},
                                                    {1, 1, 1}});
        assertEquals(-1, bg.sizeOf(0, 2));
        assertEquals(1, bg.sizeOf(0, 1));
        assertEquals(2, bg.sizeOf(0, 0));
        assertEquals(2, bg.sizeOf(1, 0));
    }

    @Test
    public void testIsStuck() {
        BubbleGrid bg = new BubbleGrid(new int[][]{{1, 0, 1},
                                                    {1, 0, 0},
                                                    {1, 1, 0},
                                                    {1, 1, 1}});
        assertTrue(bg.isStuck(0, 2));
        assertTrue(bg.isStuck(2, 0));
        assertTrue(bg.isStuck(3, 2));
    }

    @Test
    public void testPopBubbles() {
        BubbleGrid bg = new BubbleGrid(new int[][]{{1, 1, 0},
                                                    {1, 0, 0},
                                                    {1, 1, 0},
                                                    {1, 1, 1}});
        int[][] darts = new int[][]{{2, 2}, {2, 0}};
        int[] actual = bg.popBubbles(darts);
        int[] expected = new int[]{0, 4};
        assertEquals(1, bg.sizeOf(1, 0));
        assertArrayEquals(expected, actual);
        assertFalse(bg.isBubble(2, 0));

        darts = new int[][]{{1, 0}, {1, 1}};
        actual = bg.popBubbles(darts);
        expected = new int[]{0, 0};
        assertArrayEquals(expected, actual);
    }


}
