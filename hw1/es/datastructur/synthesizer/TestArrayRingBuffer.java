package es.datastructur.synthesizer;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayRingBuffer {

    @Test
    public void testArrayRingBuffer() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(3);
        assertTrue(arb.isEmpty());

        arb.enqueue(1);
        arb.enqueue(2);

        assertEquals(3, arb.capacity());
        assertEquals(2, arb.fillCount());

        arb.enqueue(3);

        assertTrue(arb.isFull());
        assertEquals(1, (int) arb.peek());
        assertEquals(1, (int) arb.dequeue());
        assertFalse(arb.isFull());
        assertFalse(arb.isEmpty());

        arb.dequeue();
        arb.enqueue(4);
        arb.enqueue(5);

        assertEquals(3, (int) arb.peek());
        assertEquals(3, (int) arb.dequeue());
        assertEquals(4, (int) arb.dequeue());
        assertEquals(5, (int) arb.dequeue());
        assertTrue(arb.isEmpty());

    }
}
