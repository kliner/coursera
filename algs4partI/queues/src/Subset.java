import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by kliner on 10/5/15.
 */
public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k == 0) {
            return;
        }
        int n = 0;
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            n++;
            if (StdRandom.uniform(n) < k) {
                if (n > k) {
                    randomizedQueue.dequeue();
                }
                randomizedQueue.enqueue(str);
            }
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}
