package hw2;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPercolation {

    @Test
    public void testBasics() {
        Percolation p = new Percolation(3);

        assertEquals(0, p.numberOfOpenSites());
        assertFalse(p.isOpen(1, 1));
        assertFalse(p.isFull(0, 2));
        assertFalse(p.percolates());
    }

    @Test
    public void testOpen() {
        Percolation p = new Percolation(3);

        p.open(1, 0);
        assertTrue(p.isOpen(1, 0));
        assertFalse(p.isFull(1, 0));
        assertFalse(p.percolates());
        assertEquals(1, p.numberOfOpenSites());

        p.open(2, 0);
        p.open(2, 1);
        assertFalse(p.isFull(0, 0));
        assertFalse(p.isFull(2, 0));
        assertFalse(p.percolates());
        assertEquals(3, p.numberOfOpenSites());

        p.open(0, 1);
        assertFalse(p.percolates());
        assertTrue(p.isFull(0, 1));
        assertFalse(p.isFull(1, 0));

        p.open(1, 1);
        assertTrue(p.percolates());
        assertFalse(p.isFull(1, 2));
    }

    @Test
    public void testBackWash() {
        Percolation p = new Percolation(3);

        p.open(1, 0);
        p.open(2, 0);
        p.open(2, 2);
        assertFalse(p.percolates());

        p.open(0, 0);
        assertTrue(p.percolates());
        assertTrue(p.isFull(2, 0));
        assertFalse(p.isFull(2, 2));
    }

}
