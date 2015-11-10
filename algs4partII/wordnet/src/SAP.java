import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.Map;

/**
 * Created by kliner on 11/8/15.
 */
public class SAP {
    private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = G.reverse().reverse();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i) && bfsV.distTo(i) + bfsW.distTo(i) < min) {
                min = bfsV.distTo(i) + bfsW.distTo(i);
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int min = Integer.MAX_VALUE;
        int a = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i) && bfsV.distTo(i) + bfsW.distTo(i) < min) {
                min = bfsV.distTo(i) + bfsW.distTo(i);
                a = i;
            }
        }
        return a;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i) && bfsV.distTo(i) + bfsW.distTo(i) < min) {
                min = bfsV.distTo(i) + bfsW.distTo(i);
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int min = Integer.MAX_VALUE;
        int a = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i) && bfsV.distTo(i) + bfsW.distTo(i) < min) {
                min = bfsV.distTo(i) + bfsW.distTo(i);
                a = i;
            }
        }
        return a;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("/Users/kliner/Downloads/wordnet/digraph3.txt");
        Digraph digraph = new Digraph(in);
        SAP sap = new SAP(digraph);
        System.out.println(sap.ancestor(0, 4));
        System.out.println(sap.ancestor(10, 13));
        System.out.println(sap.length(10, 13));
//        System.out.println(sap.ancestor(7, 3));
//        System.out.println(sap.length(7, 3));
//        System.out.println(sap.ancestor(3, 7));
//        System.out.println(sap.length(3, 7));
//        System.out.println(sap.ancestor(7, 1));
//        System.out.println(sap.length(7, 1));
//        System.out.println(sap.ancestor(7, 0));
//        System.out.println(sap.length(7, 0));
//        System.out.println(sap.ancestor(10, 11));
//        System.out.println(sap.length(10, 11));
//        System.out.println(sap.ancestor(10, 12));
//        System.out.println(sap.length(10, 12));
//        System.out.println(sap.ancestor(5, 12));
//        System.out.println(sap.length(5, 12));
//        System.out.println(sap.ancestor(0, 12));
//        System.out.println(sap.length(0, 12));
    }
}
