package es.datastructur.synthesizer;

import java.util.Iterator;

interface BoundedQueue<T> extends Iterable<T> {

    /* Return the size of the buffer. */
    int capacity();

    /* Return the number of items currently in the buffer. */
    int fillCount();

    /* Add item x to the end. */
    void enqueue(T x);

    /* Delete and return item from the front. */
    T dequeue();

    /* Return (but do not delete) item from the front. */
    T peek();

    /* Check if queue is empty. */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /* Check if queue is full. */
    default boolean isFull() {
        return capacity() == fillCount();
    }

    /* Create an iterator. */

    @Override
    Iterator<T> iterator();
}
