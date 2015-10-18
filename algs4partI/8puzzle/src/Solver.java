import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kliner on 10/16/15.
 */
public class Solver {

    private boolean     solvable;
    private int         n;
    private List<Board> solution;

    public Solver(Board initial) {
        MinPQ<MyBoard> pq = new MinPQ<>(new MinComparator());
        MinPQ<MyBoard> pq2 = new MinPQ<>(new MinComparator());
        Board twin = initial.twin();
        pq.insert(new MyBoard(initial));
        pq2.insert(new MyBoard(twin));
        n = -1;
        while (true) {
            MyBoard b1 = pq.delMin();
            MyBoard b2 = pq2.delMin();
            if (b1.board.isGoal()) {
                solvable = true;
                n = b1.getMoves();
                buildSolution(b1);
                break;
            }
            if (b2.board.isGoal()) {
                solvable = false;
                break;
            }
            for (Board t : b1.board.neighbors()) {
                MyBoard b = new MyBoard(t);
                b.setPreBoard(b1);
                b.setMoves(b1.getMoves() + 1);
                if (b1.getPreBoard() == null || !t.equals(b1.getPreBoard().board)) {
                    pq.insert(b);
                }
            }
            for (Board t : b2.board.neighbors()) {
                MyBoard b = new MyBoard(t);
                b.setPreBoard(b2);
                b.setMoves(b2.getMoves()+1);
                if (b2.getPreBoard() == null || !t.equals(b2.getPreBoard().board)) {
                    pq2.insert(b);
                }
            }
        }
    }

    private void buildSolution(MyBoard b) {
        solution = new LinkedList<>();
        MyBoard t = b;
        do {
            solution.add(0, t.board);
            t = t.getPreBoard();
        } while (t != null);
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return n;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return () -> solution.iterator();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class MyBoard {

        private Board board;
        private MyBoard preBoard;
        private int moves;

        public MyBoard(Board board) {
            this.board = board;
        }

        public MyBoard getPreBoard() {
            return preBoard;
        }

        public void setPreBoard(MyBoard preBoard) {
            this.preBoard = preBoard;
        }

        public int getMoves() {
            return moves;
        }

        public void setMoves(int moves) {
            this.moves = moves;
        }

        private int priority() {
            return moves + board.manhattan();
        }
    }

    private class MinComparator implements Comparator<MyBoard> {

        @Override
        public int compare(MyBoard o1, MyBoard o2) {
            return o1.priority() - o2.priority();
        }
    }
}
