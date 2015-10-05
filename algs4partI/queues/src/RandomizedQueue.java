import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by kliner on 10/5/15.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;
    private int cap;

    public RandomizedQueue()                 // construct an empty randomized queue
    {
        n = 0;
        cap = 1;
        a = (Item[]) new Object[cap];
    }

    public boolean isEmpty()                 // is the queue empty?
    {
        return n == 0;
    }

    public int size()                        // return the number of items on the queue
    {
        return n;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null) throw new NullPointerException();
        a[n++] = item;
        if (n == cap) {
            cap *= 2;
            resize(cap);
        }
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) throw new NoSuchElementException();
        int i = StdRandom.uniform(n);
        Item t = a[i];
        a[i] = a[n - 1];
        a[n - 1] = null;
        n--;
        if (n < cap / 4) {
            cap /= 2;
            resize(cap);
        }
        return t;
    }

    public Item sample()                     // return (but do not remove) a random item
    {
        if (isEmpty()) throw new NoSuchElementException();
        return a[StdRandom.uniform(n)];
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new RandomizedQueueIterator(a, n);
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] a;
        private int cnt;

        public RandomizedQueueIterator(Item[] a, int n) {
            this.a = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                this.a[i] = a[i];
            }
            StdRandom.shuffle(this.a);
        }

        @Override
        public boolean hasNext() {
            return cnt < a.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[cnt++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void resize(int newCap) {
        Item[] copy = (Item[]) new Object[newCap];
        for (int i = 0; i < n; i++) {
            copy[i] = a[i];
        }
        a = copy;
    }

    public static void main(String[] args)   // unit testing
    {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        System.out.println(randomizedQueue.isEmpty());
        for (int i = 1; i <= 5; i++) {
            randomizedQueue.enqueue(i);
        }
        System.out.println(randomizedQueue.size());
        for (int i : randomizedQueue) {
            System.out.println(i);
        }
        for (int i = 1; i <= 5; i++) {
            System.out.println(randomizedQueue.dequeue());
        }
        System.out.println(randomizedQueue.isEmpty());

    }
}
