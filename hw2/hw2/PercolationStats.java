package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private int T;
    private double[] thresholds;

    /* Perform T independent experiments on an N-by-N grid. */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.T = T;
        thresholds = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = pf.make(N);
            int count = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    count += 1;
                }
            }
            thresholds[i] = ((float) count) / N * N;
        }
    }

    /* Return the sample mean of the percolation threshold. */
    public double mean() {
        return StdStats.mean(thresholds);
    }

    /* Return the sample standard deviation of the percolation threshold. */
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    /* Return the low endpoint of the 95% confidence interval. */
    public double confidenceLow() {
        double mean = mean();
        double stddev = stddev();
        return mean - (1.96 * stddev / (Math.sqrt(T)));
    }

    /* Return the high endpoint of the 95% confidence interval. */
    public double confidenceHigh() {
        double mean = mean();
        double stddev = stddev();
        return mean + (1.96 * stddev / (Math.sqrt(T)));
    }

}
