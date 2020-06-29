package bearmaps;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    // helper class to pair together each item and its priority
    private class Node {
        private final T item;
        private double priority;
        Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }
    }

    private static final int INIT_CAPACITY = 4;
    private static final int RESIZE_FACTOR = 2;
    private static final double SHRINK_LIMIT = 0.75;

    private Node[] queue;                       // array of item-priority pairs at indices 1 to size
    private final Map<T, Integer> keys;         // hashMap of items and their indices
    private int size;                           // number of items in queue

    /* Creates an empty priority queue with the default initial capacity. */
    public ArrayHeapMinPQ() {
        this(INIT_CAPACITY);
    }

    /* Creates an empty priority queue with the specified initial capacity. */
    public ArrayHeapMinPQ(int capacity) {
        queue = new ArrayHeapMinPQ.Node[capacity + 1];
        keys = new HashMap<>();
        size = 0;
    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present. */
    @Override
    public void add(T item, double priority) {
        if (item == null) throw new IllegalArgumentException("First argument to add() is null");
        if (contains(item)) throw new IllegalArgumentException("Item already exists");

        // resize if necessary
        if (size == queue.length - 1) resize(queue.length * RESIZE_FACTOR);

        // add item to end of queue
        queue[++size] = new Node(item, priority);
        keys.put(item, swim(size));
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return keys.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return queue[1].item;
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");

        // resize if necessary
        if (queue.length > INIT_CAPACITY && ((double) size) / queue.length < (1 - SHRINK_LIMIT)) {
            resize(queue.length / RESIZE_FACTOR);
        }

        T result = queue[1].item;
        swap(1, size);      // swap the last item to the top
        queue[size--] = null;  // remove the last item to avoid loitering
        sink(1);         // let the new top sink to its lawful place
        keys.remove(result);
        return result;
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return size;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        int index = keys.get(item);
        if (index < 0) throw new NoSuchElementException("Item is not in priority queue");

        queue[index].priority = priority;
        index = swim(index);
        index = sink(index);
        keys.put(item, index);
    }

    /***************************************************************************
     * Helper functions.
     ***************************************************************************/

    /* Returns the final index. */
    private int swim(int index) {
        while (index > 1 && greater(index / 2, index)) {
            swap(index, index / 2);
            index /= 2;
        }
        return index > 0 ? index : 1;
    }

    /* Returns the final index. */
    private int sink(int index) {
        while (index * 2 <= size) {
            int child = index * 2;
            if (child < size && greater(child, child + 1)) child++;
            if (!greater(index, child)) break;
            swap(index, child);
            index = child;
        }
        return index;
    }

    private void swap(int x, int y) {
        Node nodeX = queue[x];
        Node nodeY = queue[y];
        queue[x] = nodeY;
        queue[y] = nodeX;
        keys.put(nodeX.item, y);
        keys.put(nodeY.item, x);
    }

    /* Compares the priority values of the two items. */
    private boolean greater(int i, int j) {
        if (i < 1 || i > size || j < 1 || j > size) {
            throw new IllegalArgumentException("Index out of bound");
        }
        return queue[i].priority - queue[j].priority > 0;
    }

    private boolean isEmpty() {
        return size() == 0;
    }

    private void resize(int capacity) {
        assert capacity > size;
        Node[] temp = new ArrayHeapMinPQ.Node[capacity + 1];
        System.arraycopy(queue, 1, temp, 1, size);
        queue = temp;
    }

}
