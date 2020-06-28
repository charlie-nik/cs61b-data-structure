public interface Deque<T> {

    /* Inserts item to the start of the deque. */
    void addFirst(T item);

    /* Inserts item to the end of the deque. */
    void addLast(T item);

    /* Removes and returns the first item of deque.
     * If no such item exists, returns null. */
    T removeFirst();

    /* Removes and returns the last item of deque.
     * If no such item exists, returns null. */
    T removeLast();

    /* Returns the item at the given index.
     * If no such item exists, returns null. */
    T get(int index);

    /* Returns the number of items in the deque. */
    int size();

    /* Returns true if deque is empty, false otherwise. */
    default boolean isEmpty() {
        return size() == 0;
    }

    /* Prints the items in the deque from front to end. */
    default void printDeque() {
        for (int i = 0; i < size(); i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }
}
