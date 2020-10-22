import edu.princeton.cs.algs4.Queue;

import org.junit.Test;

public class TestSortAlgs {

    @Test
    public void testMergeSort() {
        Queue<String> q = new Queue<>();
        q.enqueue("Joe");
        q.enqueue("Omar");
        q.enqueue("Itai");
        q.enqueue("Surprise");
        q.enqueue("Bebop");

        MergeSort.mergeSort(q);
        org.junit.Assert.assertTrue(isSorted(q));
    }

    @Test
    public void testQuickSort() {
        Queue<String> q = new Queue<>();
        q.enqueue("Joe");
        q.enqueue("Omar");
        q.enqueue("Itai");
        q.enqueue("Surprise");
        q.enqueue("Bebop");

        q = QuickSort.quickSort(q);
        org.junit.Assert.assertTrue(isSorted(q));
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable<Item>> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
