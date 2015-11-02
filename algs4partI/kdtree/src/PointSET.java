import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdIn;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by kliner on 10/28/15.
 */
public class PointSET {

    private TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }

    public boolean isEmpty() {                    // is the set empty?
        return set.isEmpty();
    }

    public int size() {                       // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) {            // add the point to the set (if it is not already in the set)
        set.add(p);
    }

    public boolean contains(Point2D p) {          // does the set contain point p?
        return set.contains(p);
    }

    public void draw() {                       // draw all points to standard draw
        set.forEach(edu.princeton.cs.algs4.Point2D::draw);
    }

    public Iterable<Point2D> range(RectHV rect) {           // all points that are inside the rectangle
        List<Point2D> l = set.stream().filter(rect::contains).collect(Collectors.toList());
        return l;
    }

    public Point2D nearest(Point2D p) {           // a nearest neighbor in the set to point p; null if the set is empty
        Point2D ret = null;
        double min = Double.MAX_VALUE;
        for (Point2D q : set) {
            if (q.distanceSquaredTo(p) < min) {
                min = q.distanceSquaredTo(p);
                ret = q;
            }
        }
        return ret;
    }

    public static void main(String[] args) {                // unit testing of the methods (optional)
        PointSET kdTree = new PointSET();
        int N = 10;
        for (int i = 0; i < N; i++) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
            System.out.println(kdTree.contains(p));
        }
        Point2D q = new Point2D(0.8, 0.2);
        System.out.println(kdTree.nearest(q));
        RectHV rect = new RectHV(0, 0, 0.4, 0.4);
        System.out.println(kdTree.range(rect));
    }
}

