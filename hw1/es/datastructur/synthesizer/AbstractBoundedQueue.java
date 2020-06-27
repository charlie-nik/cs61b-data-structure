package es.datastructur.synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {

    /* Variable for fillCount. */
    protected int fillCount;
    /* Variable for capacity. */
    protected int capacity;

    /* Return the size of queue. */
    public int capacity() {
        return capacity;
    }

    /* Return the number of items currently in queue. */
    public int fillCount() {
        return fillCount;
    }

}
