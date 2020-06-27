public class BubbleGrid {
    private final int[][] GRID;
    private final int ROW;
    private final int COL;
    private final int TOP;
    private int count;

    /* Create new BubbleGrid with bubble/space locations specified by grid.
     * Grid is composed of only 1's and 0's, where 1's denote a bubble, and
     * 0's denote a space. */
    public BubbleGrid(int[][] grid) {
        GRID = grid;
        ROW = grid.length;
        COL = grid[0].length;
        TOP = ROW * COL;

        count = update();
    }

    /* Returns an array whose i-th element is the number of bubbles that
     * fall after the i-th dart is thrown. Assume all elements of darts
     * are unique, valid locations in the grid. Must be non-destructive
     * and have no side-effects to grid. */
    public int[] popBubbles(int[][] darts) {
        int[] fallenBubbles = new int[darts.length];

        for (int i = 0; i < darts.length; i++) {
            int row = darts[i][0];
            int col = darts[i][1];
            if (GRID[row][col] == 0) {
                fallenBubbles[i] = 0;
                continue;
            }
            GRID[row][col] = 0;
            int newCount = update();
            fallenBubbles[i] = count - newCount - 1; // the popped bubble doesn't count
            count = newCount;
        }
        return fallenBubbles;
    }

    /* Return the number of remaining bubbles on the grid. */
    private int update() {
        UnionFind bubbles = new UnionFind(ROW * COL + 1);
        int result = 0;

        // top row
        for (int i = 0; i < COL; i++) {
            if (GRID[0][i] == 1) {
                bubbles.union(TOP, i);
                result += 1;
            }
        }
        for (int i = 1; i < ROW; i++) { // i == row
            for (int j = 0; j < COL; j++) { // j == column
                if (GRID[i][j] == 1) { // if position (i, j) is a bubble
                    int pos = xyTo1D(i, j);
                    if (GRID[i - 1][j] == 1) { // top bubble
                        bubbles.union(pos, pos - COL);
                    }
                    if (j > 0 && GRID[i][j - 1] == 1) { // left bubble
                        bubbles.union(pos, pos - 1);
                    }
                    if (i < ROW - 1 && GRID[i + 1][j] == 1) { // bottom bubble
                        bubbles.union(pos, pos + COL);
                    }
                    if (j < COL - 1 && GRID[i][j + 1] == 1) { // right bubble
                        bubbles.union(pos, pos + 1);
                    }

                    if (bubbles.connected(pos, TOP)) {
                        result += 1;
                    } else {
                        GRID[i][j] = 0;
                    }
                }
            }
        }
        return result;
    }

    /* helper function to convert position (x, y). */
    private int xyTo1D(int row, int col) {
        return row * GRID[0].length + col;
    }
}
