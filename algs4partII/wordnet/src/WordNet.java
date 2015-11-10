import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.*;

/**
 * Created by kliner on 11/8/15.
 */
public class WordNet {

    private Map<String, List<Integer>> map = new HashMap<>();
    private List<String> words;
    private Digraph      graph;
    private SAP          sap;
    private int          n;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new NullPointerException();
        In synIn = new In(synsets);
        words = new ArrayList<String>();
        while (synIn.hasNextLine()) {
            String s = synIn.readLine();
            String[] ss = s.split(",");
            int i = Integer.parseInt(ss[0]);
            for (String key : ss[1].split(" ")) {
                if (!map.containsKey(key)) {
                    map.put(key, new ArrayList<>());
                }
                map.get(key).add(i);
            }
            words.add(i, ss[1]);
            n++;
        }
        boolean[] notRoot = new boolean[n];
        graph = new Digraph(words.size());
        In hyperIn = new In(hypernyms);
        while (hyperIn.hasNextLine()) {
            String s = hyperIn.readLine();
            String[] ss = s.split(",");
            int v = Integer.parseInt(ss[0]);
            if (ss.length > 1) notRoot[v] = true;
            for (int i = 1; i < ss.length; i++) {
                graph.addEdge(v, Integer.parseInt(ss[i]));
            }
        }
        if (detectMultiRoot(notRoot) || detectCycle()) throw new IllegalArgumentException();
        sap = new SAP(graph);
    }

    private boolean detectMultiRoot(boolean[] notRoot) {
        int count = 0;
        for (boolean b : notRoot) if (!b) count++;
        return count != 1;
    }

    private boolean detectCycle() {
        DepthFirstOrder dfs = new DepthFirstOrder(graph);
        boolean[] flags = new boolean[n];
        for (int v : dfs.reversePost()) {
            flags[v] = true;
            for (int vj : graph.adj(v)) {
                if (flags[vj]) return true;
            }
        }
        return false;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new NullPointerException();
        return map.containsKey(word);
    }

    private Iterable<Integer> getNouns(String noun) {
        return map.get(noun);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        if (nounA == null || nounB == null) throw new NullPointerException();
        return sap.length(getNouns(nounA), getNouns(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        if (nounA == null || nounB == null) throw new NullPointerException();
        return words.get(sap.ancestor(getNouns(nounA), getNouns(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet w = new WordNet("/Users/kliner/Downloads/wordnet/synsets6.txt",
                                "/Users/kliner/Downloads/wordnet/hypernyms6TwoAncestors.txt");
        System.out.println(w.isNoun("a"));
        System.out.println(w.getNouns("a"));
        System.out.println(w.isNoun("x"));
        System.out.println(w.getNouns("x"));
        System.out.println(w.distance("a", "e"));
        System.out.println(w.sap("a", "e"));
        System.out.println(w.distance("b", "f"));
        System.out.println(w.sap("b", "f"));
        try {
            w = new WordNet("/Users/kliner/Downloads/wordnet/synsets3.txt",
                            "/Users/kliner/Downloads/wordnet/hypernyms3InvalidCycle.txt");
        } catch (Exception e) {
            System.out.println("isCycle");
        }
        try {
            w = new WordNet("/Users/kliner/Downloads/wordnet/synsets3.txt",
                            "/Users/kliner/Downloads/wordnet/hypernyms3InvalidTwoRoots.txt");
        } catch (Exception e) {
            System.out.println("not single root");
        }
        System.out.println("==========");
        w = new WordNet("/Users/kliner/Downloads/wordnet/synsets.txt", "/Users/kliner/Downloads/wordnet/hypernyms.txt");
        System.out.println(w.sap("bird", "worm"));
        System.out.println(w.distance("bird", "worm"));

        System.out.println("==========");
        System.out.println(w.distance("white_marlin", "mileage"));
        System.out.println(w.distance("American_water_spaniel", "histology"));
    }

}
