import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by kliner on 11/10/15.
 */
public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wordnet) {       // constructor takes a WordNet object
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) { // given an array of WordNet nouns, return an outcast
        int max = Integer.MIN_VALUE;
        String ret = null;
        for (String n : nouns) {
            int t = 0;
            for (String m : nouns) {
                t += wordNet.distance(n, m);
            }
            if (t > max) {
                max = t;
                ret = n;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        String path = "/Users/kliner/Downloads/wordnet/";
        WordNet wordnet = new WordNet(path + "synsets.txt", path + "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        String a = path + "outcast5.txt";
        In in = new In(a);
        String[] nouns = in.readAllStrings();
        StdOut.println(a + ": " + outcast.outcast(nouns));
    }
}
