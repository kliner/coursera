import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by kliner on 9/30/15.
 */
public class Percolation {
    private byte[][] a;
    private int n;
    private WeightedQuickUnionUF unionUF;
    private boolean perc;

    public Percolation(int N) {             // create N-by-N grid, with all sites blocked
        if (N <= 0) throw new IllegalArgumentException();
        a = new byte[N][N];
        n = N;
        unionUF = new WeightedQuickUnionUF(N * N);
        perc = false;
    }

    private void rangeCheck(int i, int j) {
        if (i <= 0 || j <= 0 || i > n || j > n) {
            throw new IndexOutOfBoundsException();
        }
    }

    private int make(int i, int j) {
        return (i - 1) * n + j - 1;
    }

    private byte find(int i) {
        return a[i / n][i % n];
    }

    public void open(int i, int j) {        // open site (row i, column j) if it is not open already
        rangeCheck(i, j);
        byte st = 1;
        if (i == 1) {
            st |= 2;
        }
        if (i == n) {
            st |= 4;
        }
        if (i > 1 && isOpen(i - 1, j)) {
            int t = unionUF.find(make(i - 1, j));
            st |= find(t);
            unionUF.union(make(i, j), make(i - 1, j));
        }
        if (i < n && isOpen(i + 1, j)) {
            int t = unionUF.find(make(i + 1, j));
            st |= find(t);
            unionUF.union(make(i, j), make(i + 1, j));
        }
        if (j > 1 && isOpen(i, j - 1)) {
            int t = unionUF.find(make(i, j - 1));
            st |= find(t);
            unionUF.union(make(i, j), make(i, j - 1));
        }
        if (j < n && isOpen(i, j + 1)) {
            int t = unionUF.find(make(i, j + 1));
            st |= find(t);
            unionUF.union(make(i, j), make(i, j + 1));
        }
        int t = unionUF.find(make(i, j));
        a[i - 1][j - 1] = st;
        a[t / n][t % n] = st;
        if (st == 7) {
            perc = true;
        }
    }

    public boolean isOpen(int i, int j) {   // is site (row i, column j) open?
        rangeCheck(i, j);
        return (a[i - 1][j - 1] & 1) == 1;
    }

    public boolean isFull(int i, int j) {   // is site (row i, column j) full?
        rangeCheck(i, j);
        int t = unionUF.find(make(i, j));
        return (a[t / n][t % n] & 2) == 2;
    }

    public boolean percolates() {           // does the system percolate?
        return perc;
    }

    public static void main(String[] args) {
        for (int N = 0; N < 1000; N++) {
            int sum = 0;
            for (int i = 1; i <= 4 * N; i = i * 4)
                for (int j = 0; j < i; j++)
                    sum++;
            System.out.println(sum);
        }

    }
}
