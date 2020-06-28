import org.junit.Test;
import static org.junit.Assert.*;

public class TestDeque {

    /* test LinkedListDeque. */
    @Test
    public void testLinkedListDeque() {
        testDeque("LinkedListDeque");

        // test getRecursive()
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addLast(3);
        lld.addLast(5);
        lld.addLast(7);                                 // (3, 5, 7)
        assertEquals(3, (int) lld.getRecursive(0));
        assertEquals(5, (int) lld.getRecursive(1));
        assertEquals(7, (int) lld.getRecursive(2));
    }

    @Test
    public void testArrayDeque() {
        testDeque("ArrayDeque");
    }

    /* a general method for testing deque data structure. */
    private void testDeque(String type) {
        Deque<Integer> test1 = makeDeque(type);

        // test isEmpty()
        assertTrue(test1.isEmpty());

        // test addFirst()
        test1.addFirst(10);
        test1.addFirst(5);                                // (5, 10)
        assertEquals(5, (int) test1.get(0));
        assertEquals(10, (int) test1.get(1));
        assertEquals(2, test1.size());

        // test addLast()
        test1.addLast(15);
        test1.addLast(20);                                // (5, 10, 15, 20)
        assertEquals(15, (int) test1.get(2));
        assertEquals(20, (int) test1.get(3));
        assertEquals(4, test1.size());
        // check if works on empty deque
        Deque<Integer> test2 = makeDeque(type);
        test2.addLast(3);
        assertEquals(3, (int) test2.get(0));

        // test removeFirst()
        assertEquals(5, (int) test1.removeFirst());   // (5, 10, 15, 20)
        assertEquals(10, (int) test1.removeFirst());  // (10, 15, 20)
        assertEquals(2, test1.size());
        // check if works on empty deque
        test2 = makeDeque(type);
        assertNull(test2.removeFirst());

        // test removeLast()
        assertEquals(20, (int) test1.removeLast());   // (15, 20)
        assertEquals(15, (int) test1.removeLast());   // (15)
        assertTrue(test1.isEmpty());
        assertNull(test1.get(0));
        // check if works on empty deque
        assertNull(test2.removeLast());

        // test get()
        test1.addLast(3);
        test1.addLast(5);
        test1.addLast(7);                                 // (3, 5, 7)
        assertEquals(3, (int) test1.get(0));
        assertEquals(5, (int) test1.get(1));
        assertEquals(7, (int) test1.get(2));
    }

    private Deque<Integer> makeDeque(String type) {
        if (type.equals("ArrayDeque")) {
            return new ArrayDeque<>();
        } else {
            return new LinkedListDeque<>();
        }
    }
}
