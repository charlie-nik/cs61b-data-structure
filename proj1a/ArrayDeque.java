public class ArrayDeque<T> {

    /* field */
    private T[] items;
    private int size;
    private int nextFirst = 0;
    private int nextLast = 1;
    private static final int RFACTOR = 2;

    /* Construct an empty array deque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
    }

    /* Construct a deep copy of another deque. */
    public ArrayDeque(ArrayDeque other) {
        items = (T[]) new Object[other.items.length];
        nextLast = 0;
        for (int i = 0; i < items.length; i++) {
            addLast((T) other.items[i]);
        }
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
        size = other.size;
    }

    /* Resize the list to target capacity. */
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

    /* Insert X into the front of the list. */
    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }

        items[nextFirst] = x;
        size += 1;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst -= 1;
        }
    }

    /* Insert X into the back of the list. */
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * RFACTOR);
        }
        items[nextLast] = x;
        size += 1;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast += 1;
        }
    }

    /* Remove and return the item at the front of the list. */
    public T removeFirst() {
        T first;
        if (nextFirst == items.length - 1) {
            first = items[0];
            nextFirst = 0;
        } else {
            first = items[nextFirst + 1];
            nextFirst += 1;
        }

        size -= 1;
        double usageRatio = (float) size / items.length;
        if (usageRatio < 0.25) {
            resize(items.length / 2);
        }

        return first;
    }

    /* Remove and return the item at the back of the list. */
    public T removeLast() {
        T last;
        if (nextLast == 0) {
            last = items[items.length - 1];
            nextLast = items.length - 1;
        } else {
            last = items[nextLast - 1];
            nextLast -= 1;
        }

        size -= 1;
        double usageRatio = (float) size / items.length;
        if (usageRatio < 0.25) {
            resize(items.length / 2);
        }

        return last;
    }

    /* Get the i-th item in the list (constant time). */
    public T get(int index) {
        int realIndex = index + nextFirst + 1;
        if (realIndex >= items.length) {
            realIndex = realIndex - items.length;
        }
        return items[realIndex];
    }

    /* Return the number of element in the list. */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

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
