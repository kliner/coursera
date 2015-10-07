import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kliner on 10/7/15.
 */
public class FastCollinearPoints {
    private ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {   // finds all line segments containing 4 or more points
        segments = new ArrayList<>();
        int n = points.length;
        Point[] temp = new Point[n];
        for (int i = 0; i < n; i++)
        {
            temp[i] = points[i];
            for (int j = i+1; j < n; j++)
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
        }

        for (Point p : points) {
            Arrays.sort(temp, p.slopeOrder());
            Point t = temp[0];
            int lo = 0;
            for (int i = 1; i < n; i++) {
                if (temp[i].slopeTo(p) != t.slopeTo(p)) {
                    if (i - lo >= 3) {
                        LineSegment segment = buildLine(temp, lo, i, p);
                        if (segment != null) {
                            segments.add(segment);
                        }
                    }
                    lo = i;
                    t = temp[i];
                }
            }
            if (n - lo >= 3) {
                LineSegment segment = buildLine(temp, lo, n, p);
                if (segment != null) {
                    segments.add(segment);
                }
            }
        }
    }

    private LineSegment buildLine(Point[] points, int lo, int hi, Point p) {
        Point min = p;
        Point max = p;
        for (int i = lo; i < hi; i++) {
            Point t = points[i];
            if (t.compareTo(max) > 0) max = t;
            if (t.compareTo(min) < 0) min = t;
        }
        if (p == min) {
            LineSegment lineSegment = new LineSegment(min, max);
            return lineSegment;
        } else {
            return null;
        }
    }

    public int numberOfSegments() {      // the number of line segments
        return segments.size();
    }

    public LineSegment[] segments() {              // the line segments
        LineSegment[] lineSegments = new LineSegment[segments.size()];
        segments.toArray(lineSegments);
        return lineSegments;
    }

    public static void main(String[] args) {
        Point[] points = new Point[16];
        points[0] = new Point(19, 10);
        points[1] = new Point(18, 10);
        points[2] = new Point(10, 10);
        points[3] = new Point(17, 10);
        points[4] = new Point(1, 1);
        points[5] = new Point(2, 3);
        points[6] = new Point(3, 5);
        points[7] = new Point(4, 7);
        points[8] = new Point(2, 2);
        points[9] = new Point(3, 3);
        points[10] = new Point(2, 1);
        points[11] = new Point(2, 9);
        points[12] = new Point(1000, 17000);
        points[13] = new Point(1000, 27000);
        points[14] = new Point(1000, 28000);
        points[15] = new Point(1000, 31000);
        FastCollinearPoints bruteCollinearPoints = new FastCollinearPoints(points);
        System.out.println(bruteCollinearPoints.numberOfSegments());

        for (LineSegment s : bruteCollinearPoints.segments()) {
            System.out.println(s.toString());
        }
    }
}
