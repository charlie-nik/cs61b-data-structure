package es.datastructur.synthesizer;
import java.util.Iterator;

class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (fillCount() == capacity()) {
            throw new RuntimeException("Ring buffer overflow");
        }

        rb[last] = x;
        last += 1;
        if (last == capacity()) {
            last = 0;
        }
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }

        T result = rb[first];
        first += 1;
        if (first == capacity()) {
            first = 0;
        }
        fillCount -= 1;
        return result;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }

    /**
     * Return the size of queue.
     */
    @Override
    public int capacity() {
        return rb.length;
    }

    /**
     * /** Return the number of items currently in queue.
     */
    @Override
    public int fillCount() {
        return fillCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) { //Don't forget NullSafe!!
            return false;
        }

        ArrayRingBuffer<T> other = (ArrayRingBuffer<T>) o;
        if (fillCount() != other.fillCount()) {
            return false;
        }
        Iterator<T> thisIterator = iterator();
        Iterator<T> otherIterator = other.iterator();
        while (thisIterator.hasNext()) {
            if (!thisIterator.next().equals(otherIterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new ARBIterator();
    }

    private class ARBIterator implements Iterator<T> {

        private int index;
        private int count;

        ARBIterator() {
            index = first;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < fillCount();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new RuntimeException("End of iteration");
            }
            T result = rb[index];
            count += 1;
            index += 1;
            if (index == capacity()) {
                index = 0;
            }
            return result;
        }
    }
}
