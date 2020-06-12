public class LinkedListDeque<T> {
    private static class LinkedList<T> {
        public T first;
        public LinkedList next;
        public LinkedList prev;

        public LinkedList(T value) {
            first = value;
        }

        public LinkedList(T value, LinkedList p, LinkedList n) {
            first = value;
            prev = p;
            next = n;
        }
    }

    // field
    private LinkedList sentinel;
    private int size;

    // Construct an empty linked list deque
    public LinkedListDeque() {
        sentinel = new LinkedList(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    // Construct a deep copy of OTHER.
    public LinkedListDeque(LinkedListDeque<T> other) {
        sentinel = new LinkedList(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = other.size;

        for (int i = 0; i < other.size; i++) {
            addLast(other.get(i));
        }
    }

    /**
     * Add an item of type T to the start of the deque.
     * @param item the value of the element to be added
     */
    public void addFirst(T item) {
        LinkedList result = new LinkedList(item, sentinel, sentinel.next);
        sentinel.prev = result;
        result.prev.next = result;
        size += 1;
    }

    /**
     * Add an item of type T to the end of the deque.
     * @param item the value of the element to be added.
     */
    public void addLast(T item) {
        LinkedList<T> result = new LinkedList(item, sentinel.prev, sentinel);
        sentinel.prev = result;
        result.prev.next = result;
        size += 1;
    }

    /**
     * Return true if deque is empty, false otherwise.
     * @return whether deque is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Return the size of the deque.
     * @return the size of deque.
     */
    public int size() {
        return size;
    }

    // Print the items in the deque from first to last.
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    /**
     * Remove and return the first item of deque.
     * If no such item exists, return null.
     * @return the value of the first item of deque
     */
    public T removeFirst() {
        T removed;
        if (!isEmpty()) {
            removed = (T) sentinel.next.first;
            sentinel.next = sentinel.next.next;
            size -= 1;
        } else {
            removed = null;
        }
        return removed;
    }

    /**
     * Remove and return the last item of deque.
     * If no such item exists, return null.
     * @return the value of the last item of deque
     */
    public T removeLast() {
        T removed;
        if (!isEmpty()) {
            removed = (T) sentinel.prev.first;
            sentinel.prev = sentinel.prev.prev;
            size -= 1;
        } else {
            removed = null;
        }
        return removed;
    }

    /**
     * Get the item at the given index.
     * If no such item exists, return null.
     * @param index the index of the desired item
     * @return the value of the desired item
     */
    public T get(int index) {
        LinkedList p = sentinel.next;
        while (p != sentinel) {
            if (index == 0) {
                return (T) p.first;
            }
            index -= 1;
            p = p.next;
        }
        return null;
    }

    // the same as get, but recursively
    public T getRecursive(int index) {
        if (index == 0) {
            return (T) sentinel.next.first;
        }
        LinkedListDeque p = new LinkedListDeque(this);
        p.removeFirst();
        return (T) p.getRecursive(index - 1);
    }
}
