import org.junit.Test;
import static org.junit.Assert.*;

public class TestUnionFind {

    @Test
    public void testBasic() {
        UnionFind uf = new UnionFind(9);

        uf.union(2, 3);
        uf.union(1, 2);
        uf.union(5, 7);
        assertEquals(3, uf.parent(1));

        uf.union(8, 4);
        uf.union(7, 2);
        assertTrue(uf.connected(1, 5));
        assertEquals(3, uf.parent(5)); // Path-compression
        assertEquals(5, uf.sizeOf(3));
        assertEquals(2, uf.sizeOf(4));
        assertEquals(4, uf.find(8));

    }
}
