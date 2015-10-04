import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by kliner on 9/30/15.
 */
public class PercolationStats {
    private double[] a;
    private int t;

    public PercolationStats(int N, int T) {   // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        t = T;
        a = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(N);
            int cnt = 0;
            while (!percolation.percolates()) {
                int x, y;
                do {
                    int rand = StdRandom.uniform(N * N);
                    x = rand / N + 1;
                    y = rand % N + 1;
                } while (percolation.isOpen(x, y));
                cnt += 1;
                percolation.open(x, y);
            }
            a[i] = 1.0 * cnt / (N*N);
        }
    }

    public double mean() {                    // sample mean of percolation threshold
        return StdStats.mean(a);
    }

    public double stddev() {                  // sample standard deviation of percolation threshold
        return StdStats.stddev(a);
    }

    public double confidenceLo() {            // low  endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(t);
    }

    public double confidenceHi() {            // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(t);
    }

    public static void main(String[] args) {  // test client (described below)
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N, T);
        System.out.println("mean\t= " + percolationStats.mean());
        System.out.println("stddev\t= " + percolationStats.stddev());
        System.out.println("95% confidence interval = " +
                percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi());
    }
}
