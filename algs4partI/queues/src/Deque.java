import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by kliner on 10/5/15.
 */
public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    private int n = 0;
    private Node head = null;
    private Node tail = null;

    public Deque()                           // construct an empty deque
    {

    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return n == 0;
    }

    public int size()                        // return the number of items on the deque
    {
        return n;
    }

    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null) throw new NullPointerException();
        Node node = new Node();
        node.item = item;
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            Node t = head;
            head = node;
            node.next = t;
            t.prev = node;
        }
        n++;
    }

    public void addLast(Item item)           // add the item to the end
    {
        if (item == null) throw new NullPointerException();
        Node node = new Node();
        node.item = item;
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        n++;
    }

    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) throw new NoSuchElementException();
        Node t = head;
        Item i = t.item;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        t = null;
        n--;
        return i;
    }

    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) throw new NoSuchElementException();
        Node t = tail;
        Item i = t.item;
        tail = tail.prev;
        if (tail != null) {
            tail.next.prev = null;
            tail.next = null;
        } else {
            head = null;
        }
        t = null;
        n--;
        return i;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node cur = head;

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node t = cur;
            cur = cur.next;
            return t.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args)   // unit testing
    {
        Deque<String> deque = new Deque<String>();
        System.out.println(deque.isEmpty());
        deque.addFirst("B");
        deque.addFirst("A");
        deque.addLast("C");
        deque.addLast("D");
        System.out.println(deque.size());
        for (String s : deque) {
            System.out.println(s);
        }
        deque.removeFirst();
        deque.removeLast();
        System.out.println(deque.size());
        for (String s : deque) {
            System.out.println(s);
        }
        deque.removeLast();
        deque.removeLast();
    }
}
