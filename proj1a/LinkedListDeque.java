import java.awt.desktop.AppReopenedEvent;

public class LinkedListDeque<T> implements Deque<T> {

    private static class LinkedList<T> {
        private final T FIRST;
        private LinkedList<T> next;
        private LinkedList<T> prev;

        LinkedList(T value) {
            FIRST = value;
        }

        LinkedList(T value, LinkedList<T> p, LinkedList<T> n) {
            FIRST = value;
            prev = p;
            next = n;
        }
    }

    /* sentinel node. */
    private final LinkedList<T> SENTINEL;
    /* number of items in the deque. */
    private int size;

    /* Constructs an empty linked list deque. */
    public LinkedListDeque() {
        SENTINEL = new LinkedList<>(null);
        SENTINEL.next = SENTINEL;
        SENTINEL.prev = SENTINEL;
        size = 0;
    }

    /* Constructs a deep copy of OTHER. */
    public LinkedListDeque(LinkedListDeque<T> other) {
        SENTINEL = new LinkedList<>(null);
        SENTINEL.next = SENTINEL;
        SENTINEL.prev = SENTINEL;
        size = 0;

        for (int i = 0; i < other.size; i++) {
            addLast(other.get(i));
        }
    }

    /* Inserts item to the start of the deque. */
    @Override
    public void addFirst(T item) {
        LinkedList<T> result = new LinkedList<>(item, SENTINEL, SENTINEL.next);
        SENTINEL.next.prev = result;
        SENTINEL.next = result;
        size += 1;
    }

    /* Inserts item to the end of the deque. */
    @Override
    public void addLast(T item) {
        LinkedList<T> result = new LinkedList<>(item, SENTINEL.prev, SENTINEL);
        SENTINEL.prev.next = result;
        SENTINEL.prev = result;
        size += 1;
    }

    /* Returns the number of items in the deque. */
    @Override
    public int size() {
        return size;
    }

    /* Removes and returns the first item of deque.
     * If no such item exists, returns null. */
    @Override
    public T removeFirst() {
        T removed = SENTINEL.next.FIRST;
        SENTINEL.next = SENTINEL.next.next;
        SENTINEL.next.prev = SENTINEL;
        size -= 1;
        return removed;
    }

    /* Removes and returns the last item of deque.
     * If no such item exists, returns null. */
    @Override
    public T removeLast() {
        T removed = SENTINEL.prev.FIRST;
        SENTINEL.prev = SENTINEL.prev.prev;
        SENTINEL.prev.next = SENTINEL;
        size -= 1;
        return removed;
    }

    /* Returns the item at the given index.
     * If no such item exists, returns null. */
    @Override
    public T get(int index) {
        if (index < 0 || index > size()) {
            throw new IllegalArgumentException();
        }
        LinkedList<T> p = SENTINEL.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.FIRST;
    }

    // the same as get, but recursively.
    public T getRecursive(int index) {
        return getRecHelper(index, SENTINEL.next);
    }

    // helper function for getRecursive().
    private T getRecHelper(int index, LinkedList<T> node) {
        if (index == 0) {
            return node.FIRST;
        }
        return getRecHelper(index - 1, node.next);
    }
}
