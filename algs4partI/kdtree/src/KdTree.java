import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdIn;

/**
 * Created by kliner on 10/28/15.
 */
public class KdTree {

    private Node root;             // root of BST

    private Point2D min;
    private double  minD;
    private int N;

    private class Node {

        private Point2D val;         // associated data
        private Node    left, right;  // left and right subtrees

        public Node(Point2D val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val.toString();
        }
    }

    public KdTree() {                             // construct an empty set of points
        N = 0;
    }

    private Node put(Node n, Point2D p, int D) {
        if (n == null) {
            N++;
            return new Node(p);
        }
        if (n.val.equals(p)) return n;
        double cmp;
        if (D == 0) cmp = p.x() - n.val.x();
        else cmp = p.y() - n.val.y();
        if (cmp < 0) n.left = put(n.left, p, D ^ 1);
        else n.right = put(n.right, p, D ^ 1);
        return n;
    }

    private Point2D get(Node n, Point2D p, int D) {
        if (n == null) return null;
        double cmp;
        if (n.val.compareTo(p) == 0) return n.val;
        if (D == 0) cmp = p.x() - n.val.x();
        else cmp = p.y() - n.val.y();
        if (cmp < 0) return get(n.left, p, D ^ 1);
        else return get(n.right, p, D ^ 1);
    }

    public boolean isEmpty() {                    // is the set empty?
        return root == null;
    }

    public int size() {                       // number of points in the set
        return N;
    }

    public void insert(Point2D p) {            // add the point to the set (if it is not already in the set)
        root = put(root, p, 0);
    }

    public boolean contains(Point2D p) {          // does the set contain point p?
        return get(root, p, 0) != null;
    }

    public void draw() {                       // draw all points to standard draw
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            x.val.draw();
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
    }

    private void search(Node p, RectHV rect, Queue<Point2D> queue, int D) {
        if (p == null) return;
        if (rect.contains(p.val)) queue.enqueue(p.val);
        if (D == 0) {
            if (p.val.x() >= rect.xmin()) search(p.left, rect, queue, D ^ 1);
            if (p.val.x() <= rect.xmax()) search(p.right, rect, queue, D ^ 1);
        } else {
            if (p.val.y() >= rect.ymin()) search(p.left, rect, queue, D ^ 1);
            if (p.val.y() <= rect.ymax()) search(p.right, rect, queue, D ^ 1);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {           // all points that are inside the rectangle
        Queue<Point2D> queue = new Queue<>();
        search(root, rect, queue, 0);
        return queue;
    }

    private void search(Node p, Point2D target, RectHV rect, int D) {
        if (p == null) return;
        if (p.val.distanceSquaredTo(target) < minD) {
            min = p.val;
            minD = min.distanceSquaredTo(target);
//            System.out.println(min);
        }
//        System.out.println(rect);
        if (D == 0) {
            RectHV rect1 = new RectHV(rect.xmin(), rect.ymin(), p.val.x(), rect.ymax());
            RectHV rect2 = new RectHV(p.val.x(), rect.ymin(), rect.xmax(), rect.ymax());
            double d1 = rect1.distanceSquaredTo(target);
            double d2 = rect2.distanceSquaredTo(target);
            if (d1 < d2) {
                if (d1 < minD) search(p.left, target, rect1, D ^ 1);
                if (d2 < minD) search(p.right, target, rect2, D ^ 1);
            } else {
                if (d2 < minD) search(p.right, target, rect2, D ^ 1);
                if (d1 < minD) search(p.left, target, rect1, D ^ 1);
            }
        } else {
            RectHV rect1 = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.val.y());
            RectHV rect2 = new RectHV(rect.xmin(), p.val.y(), rect.xmax(), rect.ymax());
            double d1 = rect1.distanceSquaredTo(target);
            double d2 = rect2.distanceSquaredTo(target);
            if (d1 < d2) {
                if (d1 < minD) search(p.left, target, rect1, D ^ 1);
                if (d2 < minD) search(p.right, target, rect2, D ^ 1);
            } else {
                if (d2 < minD) search(p.right, target, rect2, D ^ 1);
                if (d1 < minD) search(p.left, target, rect1, D ^ 1);
            }
        }
    }

    public Point2D nearest(Point2D p) {           // a nearest neighbor in the set to point p; null if the set is empty
        min = new Point2D(1e10, 1e10);
        minD = Double.MAX_VALUE;
        if (isEmpty()) return null;
        search(root, p, new RectHV(0, 0, 1e10, 1e10), 0);
        return min;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        for (int i = 0; i < 2; i++) {
            Point2D p = new Point2D(0.1, 0.1);
            kdTree.insert(p);
            System.out.println(kdTree.size());
        }
        kdTree = new KdTree();
        int N = 10;
        for (int i = 0; i < N; i++) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
            System.out.println(kdTree.contains(p));
        }
//        System.out.println(kdTree.root);
//        System.out.println(kdTree.root.right);
//        System.out.println(kdTree.root.right.left);
//        System.out.println(kdTree.root.right.left.right);
        Point2D q = new Point2D(0.8, 0.2);
        System.out.println(kdTree.nearest(q));
//        RectHV rect = new RectHV(0, 0, 0.4, 0.4);
//        System.out.println(kdTree.range(rect));
//        rect = new RectHV(0, 0, 0.5, 0.5);
//        System.out.println(kdTree.range(rect));
    }
}
