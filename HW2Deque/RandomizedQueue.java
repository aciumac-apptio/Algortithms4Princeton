/**
 * Created by eugenec on 5/6/2017.
 */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] elements;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        elements = (Item[]) new Object[1];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return size == 0;
    }
    // return the number of items on the queue

    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        if (size >= elements.length) {
            resize(2 * size);
        }

        elements[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (size > 0 && size == elements.length / 4) {
            resize(elements.length / 2);
        }

        StdRandom.shuffle(elements, 0, size);
        Item item = elements[--size];
        elements[size] = null;

        return item;
    }

    private void resize(int capacity) {
        Item[] arr = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            arr[i] = elements[i];
        }
        elements = arr;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(size);
        return elements[index];
    }

    public String toString() {
        return Arrays.toString(elements);
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private int[] order = StdRandom.permutation(size);

        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = elements[order[i++]];
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (optional)

    public static void main(String[] args) {

        System.out.println(Arrays.toString(StdRandom.permutation(10)));
        /* RandomizedQueue<String> randque = new RandomizedQueue<>();
        System.out.println("Engueuing items \"to be or not\"");
        randque.enqueue("to");
        System.out.println(randque.toString());
        randque.enqueue("be");
        System.out.println(randque.toString());
        randque.enqueue("or");
        System.out.println(randque.toString());
        randque.enqueue("not");
        System.out.println(randque.toString());
        System.out.println("Sampling random items ");
        System.out.println(randque.sample());
        System.out.println(randque.toString());
        System.out.println(randque.sample());
        System.out.println(randque.toString());
        System.out.println(randque.sample());
        System.out.println(randque.toString());*/

        /* System.out.println("Degueuing random items ");
        System.out.println(randque.dequeue());
        System.out.println(randque.toString());
        System.out.println(randque.dequeue());
        System.out.println(randque.toString());*/
    }
}