import edu.princeton.cs.algs4.*;

import java.util.*;

/**
 * Created by kliner on 11/22/15.
 */
public class BaseballElimination {

    private final int                  n;
    private final List<String>         teams;
    private final Map<String, Integer> map;
    private final int[]                wins;
    private final int[]                losses;
    private final int[]                remaining;
    private final int[][]              against;
    private final boolean[]            eliminated;
    private final List[]               certs;

    private class MyHashMap extends HashMap<String, Integer> {

        @Override
        public Integer get(Object key) {
            if (containsKey(key)) return super.get(key);
            else throw new IllegalArgumentException();
        }
    }

    public BaseballElimination(
            String filename) {                  // create a baseball division from given filename in format specified below
        In in = new In(filename);
        n = Integer.parseInt(in.readLine());
        teams = new ArrayList<>(n);
        map = new MyHashMap();
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        against = new int[n][n];
        eliminated = new boolean[n];
        certs = new List[n];
        for (int i = 0; i < n; i++) {
            String t = in.readString();
            teams.add(t);
            map.put(t, i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                against[i][j] = in.readInt();
            }
        }

        for (int i = 0; i < n; i++) {
            try {
                FlowNetwork flowNetwork = buildNetwork(i);
                FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);

                for (int j = 1; j <= (n - 1) * (n - 2) / 2; j++) {
                    if (fordFulkerson.inCut(j)) {
                        eliminated[i] = true;
                        certs[i] = new ArrayList<>();
                        for (int k = 0; k < n; k++) {
                            if (fordFulkerson.inCut(k + (n - 1) * (n - 2) / 2 + 1)) {
                                if (k < i) certs[i].add(teams.get(k));
                                else certs[i].add(teams.get(k+1));

                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                eliminated[i] = true;
                certs[i] = new ArrayList<>();
                certs[i].add(teams.get(Integer.parseInt(e.getMessage())));
            }
        }
    }

    private int calc(int i, int j, int k) {
        if (i > k) i--;
        if (j > k) j--;
        return (j - i) + (n - i + n - 3) * i / 2;
//        if (i > k && j > k) return (i - 1) * (n - 2) + j - 1;
//        if (i > k) return (i - 1) * (n - 2) + j;
//        if (j > k) return i * (n - 2) + j - 1;
//        return i * (n - 2) + j;
    }

    private int calc(int i, int k) {
        if (i > k) return (n - 1) * (n - 2) / 2 + i;
        return (n - 1) * (n - 2) / 2 + i + 1;
    }

    private FlowNetwork buildNetwork(int k) throws Exception {
        FlowNetwork flowNetwork = new FlowNetwork((n - 1) * (n - 2) / 2 + 1 + n);
        for (int i = 0; i < n; i++) {
            if (i == k) continue;
            for (int j = i + 1; j < n; j++) {
                if (j == k) continue;
//                System.out.println("" + i + ',' + j + ',' + k + ',' + calc(i, j, k));
                flowNetwork.addEdge(new FlowEdge(0, calc(i, j, k), against[i][j]));
                flowNetwork.addEdge(new FlowEdge(calc(i, j, k), calc(i, k), Double.MAX_VALUE));
                flowNetwork.addEdge(new FlowEdge(calc(i, j, k), calc(j, k), Double.MAX_VALUE));
            }
//            System.out.println("" + i + ',' + k + ',' + calc(i, k));
            int t = wins[k] + remaining[k] - wins[i];
            if (t < 0) throw new Exception("" + i);
            flowNetwork.addEdge(new FlowEdge(calc(i, k), flowNetwork.V() - 1, t));
        }
        return flowNetwork;
    }

    public int numberOfTeams() {                      // number of teams
        return n;
    }

    public Iterable<String> teams() {                              // all teams
        return teams;
    }

    public int wins(String team) {                    // number of wins for given team
        return wins[map.get(team)];
    }

    public int losses(String team) {                  // number of losses for given team
        return losses[map.get(team)];
    }

    public int remaining(String team) {               // number of remaining games for given team
        return remaining[map.get(team)];
    }

    public int against(String team1, String team2) {  // number of remaining games between team1 and team2
        return against[map.get(team1)][map.get(team2)];
    }

    public boolean isEliminated(String team) {            // is given team eliminated?
        return eliminated[map.get(team)];
    }

    public Iterable<String> certificateOfElimination(
            String team) {  // subset R of teams that eliminates given team; null if not eliminated
        return certs[map.get(team)];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("/Users/kliner/Downloads/baseball/teams4a.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
