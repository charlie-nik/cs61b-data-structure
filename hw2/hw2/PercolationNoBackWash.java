package hw2;

public class PercolationNoBackWash extends Percolation {

    private boolean[] connectTop;
    private boolean[] connectBottom;
    private boolean isPercolated;

    public PercolationNoBackWash(int N) {
        super(N);
        connectTop = new boolean[N * N];
        connectBottom = new boolean[N * N];
        isPercolated = false;
    }

    @Override
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;
        }

        int pos = xyTo1D(row, col);
        boolean top = false;
        boolean bottom = false;
        if (row == 0 || connectTop[grid.find(pos)]) {
            top = true;
        }
        if (row == side - 1 || connectBottom[grid.find(pos)]) {
            bottom = true;
        }

        if (row > 0 && isOpen(row - 1, col)) {
            grid.union(pos, pos - side);
            if (connectTop[grid.find(pos - side)]) {
                top = true;
            }
            if (connectBottom[grid.find(pos - side)]) {
                bottom = true;
            }
        }
        if (col > 0 && isOpen(row, col - 1)) {
            grid.union(pos, pos - 1);
            if (connectTop[grid.find(pos - 1)]) {
                top = true;
            }
            if (connectBottom[grid.find(pos - 1)]) {
                bottom = true;
            }
        }
        if (row < side - 1 && isOpen(row + 1, col)) {
            grid.union(pos, pos + side);
            if (connectTop[grid.find(pos + side)]) {
                top = true;
            }
            if (connectBottom[grid.find(pos + side)]) {
                bottom = true;
            }
        }
        if (col < side - 1 && isOpen(row, col + 1)) {
            grid.union(pos, pos + 1);
            if (connectTop[grid.find(pos + 1)]) {
                top = true;
            }
            if (connectBottom[grid.find(pos + 1)]) {
                bottom = true;
            }
        }

        if (top && bottom) {
            isPercolated = true;
        }
        connectTop[pos] = top;
        connectBottom[pos] = bottom;
        openSitesArray[row][col] = true;
        openSitesNum += 1;
    }

    @Override
    public boolean isFull(int row, int col) {
        validate(row, col);
        int pos = xyTo1D(row, col);
        return connectTop[grid.find(pos)];
    }

    @Override
    public boolean percolates() {
        return isPercolated;
    }
}
