/**
 * Created by eugenec on 4/30/2017.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node oldFirst = first;
        // Need to create new node to avoid circular link
        Node node = new Node();
        node.item = item;
        node.next = oldFirst;
        this.first = node;
        if (size == 0) {
            last = first;
        }
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node oldLast = last;
        Node node = new Node();
        node.item = item;
        node.next = null;
        if (isEmpty()) {
            first = node;
        } else {
            oldLast.next = node;
        }
        this.last = node;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node oldFirst = first;
        Item item = oldFirst.item;
        first = first.next;
        if (size == 1) {
            last = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = last.item;
        if (size == 1) {
            return removeFirst();
        } else if (size == 2) {
            last = first;
            first.next = null;
            size--;
        } else {
            Node current = first;
            while (current.next != last) {
                current = current.next;
            }

            current.next = null;
            last = current;
            size--;
        }

        return item;
    }

    // Helping method
    public String toString() {
        if (!isEmpty()) {
            StringBuilder str = new StringBuilder();
            str.append("[");
            Node current = first;
            // if size == 1?
            if (size == 1) {
                str.append(current.item.toString() + "]");
            } else {
                while (current != null) {
                    str.append(current.item.toString() + ", ");
                    current = current.next;
                }
                str.delete(str.length() - 2, str.length());
                str.append("]");
            }
            return str.toString();
        } else {
            return "[]";
        }
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    private class Node {
        private Item item;
        private Node next;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addLast("to");
        System.out.println(deque.toString());
        deque.addLast("be");
        System.out.println(deque.toString());
        deque.addLast("or");
        System.out.println(deque.toString());
        deque.addFirst("to");
        System.out.println(deque.toString());
        deque.addFirst("be");
        System.out.println(deque.toString());
        deque.addFirst("or");
        System.out.println(deque.toString());
    }

}
