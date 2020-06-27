package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF grid;
    /* employ a second WQUUF in order to avoid backwash. */
    private WeightedQuickUnionUF backwashGrid;
    /* the size of the grid. */
    private int side;
    /* the number of open sites. */
    private int openSitesNum;
    /* an array of sites, true if open, false if closed. */
    private boolean[][] openSitesArray;
    /* a virtual top site connected to all open sites in top row. */
    private int virtualTop;
    /* a virtual bottom site connected to all open sites in bottom row. */
    private int virtualBottom;

    /* Create N-by-N grid, with all sites initially blocked. */
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        grid = new WeightedQuickUnionUF(N * N + 2);
        backwashGrid = new WeightedQuickUnionUF(N * N + 1);
        side = N;
        openSitesNum = 0;
        openSitesArray = new boolean[N][N];
        virtualTop = N * N;
        virtualBottom = N * N + 1;
    }

    /* Open the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;
        }

        int pos = xyTo1D(row, col);
        // if the site is in top or bottom row, connect it to the corresponding virtual site.
        if (row == 0) {
            grid.union(pos, virtualTop);
            backwashGrid.union(pos, virtualTop);
        }
        if (row == side - 1) {
            grid.union(pos, virtualBottom);
        }

        // if the site's top, bottom, left, right sites are open, connect it to them.
        if (row > 0 && isOpen(row - 1, col)) {
            grid.union(pos, pos - side);
            backwashGrid.union(pos, pos - side);
        }
        if (col > 0 && isOpen(row, col - 1)) {
            grid.union(pos, pos - 1);
            backwashGrid.union(pos, pos - 1);
        }
        if (row < side - 1 && isOpen(row + 1, col)) {
            grid.union(pos, pos + side);
            backwashGrid.union(pos, pos + side);
        }
        if (col < side - 1 && isOpen(row, col + 1)) {
            grid.union(pos, pos + 1);
            backwashGrid.union(pos, pos + 1);
        }

        // increment openSitesNum and update openSitesArray accordingly.
        openSitesNum += 1;
        openSitesArray[row][col] = true;
    }

    /* Return true if the site (row, col) is open, false if otherwise. */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return openSitesArray[row][col];
    }

    /* Return true if the site (row, col) is full, false if otherwise. */
    public boolean isFull(int row, int col) {
        validate(row, col);
        int pos = xyTo1D(row, col);
        return backwashGrid.connected(pos, virtualTop);
    }

    /* Return the number of open sites. */
    public int numberOfOpenSites() {
        return openSitesNum;
    }

    /* Return true if the system percolates, false if otherwise. */
    public boolean percolates() {
        return grid.connected(virtualTop, virtualBottom);
    }

    /* Throw exception if the given argument is outside the prescribed range. */
    private void validate(int row, int col) {
        if (row < 0 || row >= side || col < 0 || col >= side) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    /* Translate a two-argument site into a one-argument one. */
    private int xyTo1D(int row, int col) {
        return row * side + col;
    }

    /* for testing purpose. */
    public static void main(String[] args) {
    }

}
