import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by kliner on 10/16/15.
 */
public class Board {

    private static int[] dx = { 0, 1, 0, -1 };
    private static int[] dy = { 1, 0, -1, 0 };
    private int[][] blocks;
    private int ox, oy;

    public Board(int[][] blocks) {
        int n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    ox = i;
                    oy = j;
                }
            }
    }

    private void swap(int a, int b, int c, int d) {
        int t = blocks[a][b];
        blocks[a][b] = blocks[c][d];
        blocks[c][d] = t;
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        int ret = 0;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (blocks[i][j] != i * dimension() + j + 1 && blocks[i][j] != 0) ret++;
        return ret;
    }

    public int manhattan() {
        int ret = 0;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                int t = blocks[i][j] - 1;
                if (t == -1) continue;
                int ti = t / dimension();
                int tj = t % dimension();
                ret += Math.abs(ti-i) + Math.abs(tj - j);
            }
        return ret;
    }

    public boolean isGoal() {
        return manhattan() == 0;
    }

    public Board twin() {
        int ti = 0, tj = 0;
        boolean b = true;
        Board ret = null;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != 0) {
                    if (b) {
                        ti = i;
                        tj = j;
                        b = false;
                    } else {
                        swap(i, j, ti, tj);
                        ret = new Board(blocks);
                        swap(i, j, ti, tj);
                        return ret;
                    }
                }
            }
        return ret;
    }

    public boolean equals(Object y) {
        if (y == null || !(y.getClass() == Board.class)) return false;
        Board b = (Board) y;
        if (b.dimension() != dimension()) return false;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != b.blocks[i][j]) return false;
            }
        return true;
    }

    public Iterable<Board> neighbors() {
        return new BoardNeighbors(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension()).append('\n');
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sb.append(' ');
                if (i == ox && j == oy)
                    sb.append('0');
                else
                    sb.append(blocks[i][j]);
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private class BoardNeighbors implements Iterable<Board> {

        private ArrayList<Board> list = new ArrayList<>();

        public BoardNeighbors(Board board) {
            for (int i = 0; i < 4; i++) {
                int x = board.ox + board.dx[i];
                int y = board.oy + board.dy[i];
                if (x >= 0 && x < board.dimension() && y >= 0 && y < board.dimension()) {
                    board.swap(board.ox, board.oy, x, y);
                    list.add(new Board(board.blocks));
                    board.swap(board.ox, board.oy, x, y);
                }
            }
        }

        @Override
        public Iterator<Board> iterator() {
            return new Iterator<Board>() {

                private int cnt = 0;

                @Override
                public boolean hasNext() {
                    return cnt < list.size();
                }

                @Override
                public Board next() {
                    return list.get(cnt++);
                }
            };
        }
    }

    public static void main(String[] args) {
        int[][] blocks = new int[3][3];
        blocks[0][1] = 1;
        blocks[0][2] = 3;
        blocks[1][0] = 4;
        blocks[1][1] = 2;
        blocks[1][2] = 5;
        blocks[2][0] = 7;
        blocks[2][1] = 8;
        blocks[2][2] = 6;
        Board board = new Board(blocks);
        System.out.println(board);
        System.out.println(board.manhattan());
        System.out.println(board.twin());
        for (Board b : board.neighbors()) {
            System.out.println(b);
        }
    }
}
