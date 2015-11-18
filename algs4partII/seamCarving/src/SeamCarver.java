import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by kliner on 11/16/15.
 */
public class SeamCarver {

    private int[][]  colors;
    private byte[][] path;
    private boolean  isHori;
    private int      h;
    private int      w;
    private double[] dist;
    private double[] cur;

    public SeamCarver(Picture picture) {              // create a seam carver object based on the given picture
        w = picture.width();
        h = picture.height();
        int x = Math.max(w, h);
        colors = new int[x][x];
        path = new byte[x][x];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                colors[i][j] = picture.get(j, i).getRGB();
            }
        }
        isHori = false;
        cur = new double[x];
        dist = new double[x];
    }

    private void initDist() {
        for (int i = 0; i < w; i++) {
            dist[i] = calcEng(i, 0);
        }
    }

    public Picture picture() {                        // current picture
        if (isHori) {
            transpose();
        }
        Picture p = new Picture(w, h);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                p.set(i, j, new Color(colors[j][i]));
            }
        }
        return p;
    }

    public int width() {                          // width of current picture
        if (isHori) return h;
        return w;
    }

    public int height() {                         // height of current picture
        if (isHori) return w;
        return h;
    }

    /**
     * @param x width
     * @param y height
     * @return
     */
    private double calcEng(int x, int y) {
        if (x == 0 || x == w - 1 || y == 0 || y == h - 1) {
            return 1000;
        } else {
            double r = 0;
            r += dc(colors[y][x - 1], colors[y][x + 1]);
            r += dc(colors[y - 1][x], colors[y + 1][x]);
            return Math.sqrt(r);
        }
    }

    public double energy(int x, int y) {             // energy of pixel at column x and row y
        if (x < 0 || x >= width() || y < 0 || y >= height()) throw new IndexOutOfBoundsException();
        if (isHori) return calcEng(y, x);
        return calcEng(x, y);
    }

    private int getRedD(int c1, int c2) {
        int t = getRed(c1) - getRed(c2);
        return t * t;
    }

    private int getGreenD(int c1, int c2) {
        int t = getGreen(c1) - getGreen(c2);
        return t * t;
    }

    private int getBlueD(int c1, int c2) {
        int t = getBlue(c1) - getBlue(c2);
        return t * t;
    }

    private int getRed(int c) {
        return (c >> 16) & 0xFF;
    }

    private int getGreen(int c) {
        return (c >> 8) & 0xFF;
    }

    private int getBlue(int c) {
        return c & 0xFF;
    }

    private int dc(int c1, int c2) {
        int r = 0;
        r += getRedD(c1, c2);
        r += getGreenD(c1, c2);
        r += getBlueD(c1, c2);
        return r;
    }

    public int[] findHorizontalSeam() {             // sequence of indices for horizontal seam
        if (isHori) {
            return findSeam();
        } else {
            transpose();
            return findSeam();
        }
    }

    private void transpose() {
        isHori = !isHori;
        int t = w;
        w = h;
        h = t;
        int[][] tc = new int[h][w];
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                tc[i][j] = colors[j][i];
        colors = tc;
    }

    /**
     * relax line i
     * @param i height
     * @param last dist[height-1][]
     * @param cur dist[height]
     */
    private void relax(int i, double[] last, double[] cur) {
        for (int j = 0; j < w; j++) {
            cur[j] = Double.MAX_VALUE;
        }
        for (int j = 0; j < w; j++) {
            double t = calcEng(j, i);
            if (j > 0 && last[j - 1] + t < cur[j]) {
                cur[j] = last[j - 1] + t;
                path[i][j] = -1;
            }
            if (last[j] + t < cur[j]) {
                cur[j] = last[j] + t;
                path[i][j] = 0;
            }
            if (j < w - 1 && last[j + 1] + t < cur[j]) {
                cur[j] = last[j + 1] + t;
                path[i][j] = 1;
            }
        }
    }

    private void print(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print("" + a[i][j] + ",");
            }
            System.out.println();
        }
    }

    private void print(Color[][] a) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                System.out.print("" + a[i][j] + ",");
            }
            System.out.println();
        }
    }

    private void print(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print("" + a[i][j] + ",");
            }
            System.out.println();
        }
    }

    private int[] findSeam() {
        if (w <= 2) {
            return new int[h];
        }
        initDist();
        for (int i = 1; i < h; i++) {
            relax(i, dist, cur);
            double[] t = dist;
            dist = cur;
            cur = t;
        }
        double min = Double.MAX_VALUE;
        int mini = -1;
        for (int i = 0; i < w; i++) {
            if (dist[i] < min) {
                min = dist[i];
                mini = i;
            }
        }
        int[] ret = new int[h];
        Stack<Integer> stack = new Stack<>();
        stack.push(mini);
        for (int i = h - 1; i > 0; i--) {
            mini += path[i][mini];
            stack.push(mini);
        }
        Iterator<Integer> iterator = stack.iterator();
        for (int i = 0; i < h; i++)
            ret[i] = iterator.next();
        return ret;
    }

    public int[] findVerticalSeam() {               // sequence of indices for vertical seam
        if (isHori) {
            transpose();
            return findSeam();
        } else {
            return findSeam();
        }
    }

    private void removeSeam(int[] seam) {
        if (w <= 1 || seam.length != h) throw new IllegalArgumentException();
        int t = seam[0];
        for (int s : seam) {
            if (s < 0 || s >= w || Math.abs(s - t) > 1) throw new IllegalArgumentException();
            t = s;
        }
        for (int i = 0; i < h; i++) {
            int s = seam[i];
            int b = 0;
            for (int j = 0; j < w - 1; j++) {
                if (s == j) b = 1;
                colors[i][j] = colors[i][j + b];
            }
        }
        w--;
    }

    public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from current picture
        if (!isHori) {
            transpose();
        }
        removeSeam(seam);
    }

    public void removeVerticalSeam(int[] seam) {   // remove vertical seam from current picture
        if (isHori) {
            transpose();
        }
        removeSeam(seam);
    }

    public static void main(String[] args) {
        Picture picture = new Picture("/Users/kliner/Downloads/seamCarving/6x5.png");
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        SeamCarver carver = new SeamCarver(picture);
        int[] rm = new int[picture.height()];
        int n = 0;
        for (int t : carver.findVerticalSeam()) {
            StdOut.print(t);
            rm[n++] = t;
        }
        System.out.println();
        StdOut.println(carver.energy(1, 2));
        for (int i : carver.findHorizontalSeam()) {
            StdOut.print(i);
        }
        System.out.println();
        StdOut.println(carver.energy(1, 2));
        carver.removeVerticalSeam(rm);
        carver.print(carver.colors);
        for (int i : carver.findVerticalSeam()) {
            StdOut.print(i);
        }
        System.out.println();
    }
}
