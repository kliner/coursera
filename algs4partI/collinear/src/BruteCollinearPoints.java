import java.util.Arrays;

/**
 * Created by kliner on 10/7/15.
 */
public class BruteCollinearPoints {
    private final LineSegment[] segments;
    private int num;

    public BruteCollinearPoints(Point[] points) {  // finds all line segments containing 4 points
        int n = points.length;
        LineSegment[] lineSegments = new LineSegment[n];

        for (int p = 0; p < n; p++) {
            for (int q = p+1; q < n; q++) {
                if (points[p].slopeTo(points[q]) == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();
                for (int r = q+1; r < n; r++) {
                    for (int s = r+1; s < n; s++) {
                        if (points[p].slopeTo(points[q]) == points[p].slopeTo(points[r]) &&
                                points[p].slopeTo(points[q]) == points[p].slopeTo(points[s])) {
                            lineSegments[num++] = buildLine(points[p], points[q], points[r], points[s]);
                        }
                    }
                }
            }
        }
        segments = new LineSegment[num];

        for (int i = 0; i < num; i++) {
            segments[i] = lineSegments[i];
        }
        lineSegments = null;
    }

    private LineSegment buildLine(Point p, Point q, Point r, Point s) {
        Point[] t = new Point[4];
        t[0] = p;
        t[1] = q;
        t[2] = r;
        t[3] = s;
        Arrays.sort(t);
        LineSegment lineSegment = new LineSegment(t[0], t[3]);
        t = null;
        return lineSegment;
    }

    public int numberOfSegments() {      // the number of line segments
        return num;
    }

    public LineSegment[] segments() {              // the line segments
        return Arrays.copyOf(segments, segments.length);
    }

    public static void main(String[] args) {
        Point[] points = new Point[10];
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
        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        System.out.println(bruteCollinearPoints.numberOfSegments());

        for (LineSegment s : bruteCollinearPoints.segments()) {
            System.out.println(s.toString());
        }
    }
}
