public class ArrayDeque<T> implements Deque<T> {

    /* field */
    private static final int INIT_CAPACITY = 8;
    private T[] items;
    private int size;
    private int nextFirst = 0;
    private int nextLast = 1;
    private static final int RFACTOR = 2;

    /* Constructs an empty array deque. */
    public ArrayDeque() {
        items = (T[]) new Object[INIT_CAPACITY];
        size = 0;
    }

    /* Constructs a deep copy of another deque. */
    public ArrayDeque(ArrayDeque<T> other) {
        items = (T[]) new Object[other.items.length];
        System.arraycopy(other.items, 0, items, 0, items.length);
        size = other.size;
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
    }

    /* Resizes the list to target capacity. */
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];

        if (nextFirst < nextLast - 1 || nextLast == 0) {
            System.arraycopy(items, nextFirst + 1, temp, 1, size);
        } else if (nextFirst == items.length - 1) {
            System.arraycopy(items, 0, temp, 1, size);
        } else {
            int firstPart = items.length - nextFirst - 1;
            System.arraycopy(items, nextFirst + 1, temp, 1, firstPart);
            System.arraycopy(items, 0, temp, firstPart + 1, size - firstPart);
        }
        nextFirst = 0;
        nextLast = size + 1;
        items = temp;
    }

    /* Inserts X into the front of the deque. */
    @Override
    public void addFirst(T item) {
        // double list length if full
        if (size == items.length) {
            resize(size * RFACTOR);
        }

        items[nextFirst] = item;
        size += 1;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst -= 1;
        }
    }

    /* Inserts item into the back of the deque. */
    @Override
    public void addLast(T item) {
        // double list length if full
        if (size == items.length) {
            resize(size * RFACTOR);
        }

        items[nextLast] = item;
        size += 1;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast += 1;
        }
    }

    /* Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T first;
        if (nextFirst == items.length - 1) {
            first = items[0];
            nextFirst = 0;
        } else {
            first = items[nextFirst + 1];
            nextFirst += 1;
        }
        size -= 1;

        // halve list if less than 25% full
        double usageRatio = ((float) size) / items.length;
        if (items.length > INIT_CAPACITY && usageRatio < 0.25) {
            resize(items.length / 2);
        }

        return first;
    }

    /* Removes and returns the item at the end of the deque.
     * If no such item exists, returns null. */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        T last;
        if (nextLast == 0) {
            last = items[items.length - 1];
            nextLast = items.length - 1;
        } else {
            last = items[nextLast - 1];
            nextLast -= 1;
        }
        size -= 1;

        // halve list if less than 25% full
        double usageRatio = ((float) size) / items.length;
        if (items.length > INIT_CAPACITY && usageRatio < 0.25) {
            resize(items.length / 2);
        }

        return last;
    }

    /* Returns the item at the given index.
     * If no such item exists, returns null. */
    @Override
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException();
        }

        int realIndex = index + nextFirst + 1;
        if (realIndex >= items.length) {
            realIndex = realIndex - items.length;
        }
        return items[realIndex];
    }

    /* Returns the number of element in the deque. */
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (nextFirst < nextLast - 1 || nextLast == 0) {
            for (int i = nextFirst + 1; i < nextLast; i++) {
                System.out.print(items[i] + " ");
            }
        } else if (nextFirst == items.length - 1) {
            for (int i = 0; i < nextLast; i++) {
                System.out.print(items[i] + " ");
            }
        } else {
            int firstPart = items.length - nextFirst - 1;
            for (int i = nextFirst + 1; i < items.length; i++) {
                System.out.print(items[i] + " ");
            }
            for (int i = 0; i < nextLast; i++) {
                System.out.print(items[i] + " ");
            }
        }

        System.out.println();
    }
}
