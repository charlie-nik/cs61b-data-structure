public class BubbleGrid {

    public int[][] grid;
    private int[][][][] parents;
    private int[][] sizes;

    /**
     * Creates a bubble grid with 1s and 0s.
     */
    public BubbleGrid(int[][] grid) {
        this.grid = grid;
        sizes = new int[grid.length][grid[0].length];
        parents = new int[grid.length][grid[0].length][4][2];
        union(grid);
    }

    /**
     * Returns true if the given location is a bubble,
     * false if it is a space.
     */
    public boolean isBubble(int x, int y) {
        return grid[x][y] == 1;
    }

    /**
     * Returns the numbers of bubble orthogonally adjacent to the input position.
     * If the input position is space, returns -1.
     */
    public int sizeOf(int x, int y) {
        return sizes[x][y];
    }

    /**
     * Assigns the numbers of bubble orthogonally adjacent to each position in the grid.
     * If the input position is space, returns -1.
     */
    public void union(int[][] grid) {
        for (int i = 0; i < grid[0].length; i++) {
            parents[0][i][0] = new int[]{0, i};
        }
        for (int i = 1; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!isBubble(i, j)) {
                    sizes[i][j] = -1;
                    break;
                }

                int count = 0;
                if (isBubble(i - 1, j)) {
                    count += 1;
                    parents[i][j][0] = new int[]{i - 1, j};
                }
                if (j > 0 && isBubble(i, j - 1)) {
                    count += 1;
                    parents[i][j][1] = new int[]{i, j - 1};
                }
                if (i < grid.length - 1 && isBubble(i + 1, j)) {
                    count += 1;
                    parents[i][j][2] = new int[]{i + 1, j};
                }
                if (j < grid[i].length - 1 && isBubble(i, j + 1)) {
                    count += 1;
                    parents[i][j][3] = new int[]{i, j + 1};
                }
                sizes[i][j] = count;
            }
        }
    }

    /**
     * Returns true if the given bubble is stuck,
     * false if otherwise.
     */
    public boolean isStuck(int x, int y) {
        // Bubble is in the topmost row of the grid.
        if (x == 0) {
            return true;
        }
        // Bubble is orthogonally adjacent to other stuck bubbles.
        return sizes[x][y] > 0;
    }

    /**
     * Returns the number of bubbles that will fall
     * when the bubble at position(x, y) becomes popped or unstuck.
     * This method should be recursive.
     */
    public int unstuck(int x, int y){
        int count = 0;
        if (x > 0 && isBubble(x - 1, y)) {
            sizes[x - 1][y] -= 1;
            if (!isStuck(x - 1, y)) {
                grid[x - 1][y] = 0;
                count += 1 + unstuck(x - 1, y);
            }
        }
        if (y > 0 && isBubble(x, y - 1)) {
            sizes[x][y - 1] -= 1;
            if (!isStuck(x, y - 1)) {
                grid[x][y - 1] = 0;
                count += 1 + unstuck(x, y - 1);
            }
        }
        if (x < grid.length - 1 && isBubble(x + 1, y)) {
            sizes[x + 1][y] -= 1;
            if (!isStuck(x + 1, y)) {
                grid[x + 1][y] = 0;
                count += 1 + unstuck(x + 1, y);
            }
        }
        if (y < grid[x].length - 1 && isBubble(x, y + 1)) {
            sizes[x][y + 1] -= 1;
            if (!isStuck(x, y + 1)) {
                grid[x][y + 1] = 0;
                count += 1 + unstuck(x, y + 1);
            }
        }
        return count;
    }

    public int[] popBubbles(int[][] darts) {
        int[] fallenBubbles = new int[darts.length];

        for (int i = 0; i < darts.length; i++) {
            int x = darts[i][0];
            int y = darts[i][1];
            if (!isBubble(x, y)) {
                fallenBubbles[i] = 0;
                continue;
            }
            grid[x][y] = 0;
            sizes[x][y] = 0;
            fallenBubbles[i] = unstuck(x, y);
        }

        return fallenBubbles;
    }

}
