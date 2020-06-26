import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import static org.junit.Assert.*;
import org.junit.Test;

/** Tests of optional parts of lab 8. */
public class TestBSTMapExtra {

    /*
    * Sanity test for keySet, only here because it's optional
    */
    @Test
    public void sanityKeySetTest() {
    	BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        HashSet<String> values = new HashSet<String>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
            values.add("hi" + i);
        }
        assertEquals(455, b.size()); //keys are there
        Set<String> keySet = b.keySet();
        assertTrue(values.containsAll(keySet));
        assertTrue(keySet.containsAll(values));
    }

    /* Remove Test
     *
     * Note for testRemoveRoot:
     *
     * Just checking that c is gone (perhaps incorrectly)
     * assumes that remove is BST-structure preserving.
     *
     * More exhaustive tests could be done to verify
     * implementation of remove, but that would require doing
     * things like checking for inorder vs. preorder swaps,
     * and is unnecessary in this simple BST implementation.
     */
    @Test
    public void testRemoveRoot() {
        BSTMap<String,String> q = new BSTMap<String,String>();
        q.put("c","a");
        q.put("b","a");
        q.put("a","a");
        q.put("d","a");
        q.put("e","a"); // a b c d e
        assertTrue(null != q.remove("c"));
        assertFalse(q.containsKey("c"));
        assertTrue(q.containsKey("a"));
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("d"));
        assertTrue(q.containsKey("e"));
    }

    /* Remove Test 2
     * test the 3 different cases of remove
     */
    @Test
    public void testRemoveThreeCases() {
        BSTMap<String,String> q = new BSTMap<String,String>();
        q.put("c","a");
        q.put("b","a");
        q.put("a","a");
        q.put("d","a");
        q.put("e","a");                         // a b c d e
        assertTrue(null != q.remove("e"));      // a b c d
        assertTrue(q.containsKey("a"));
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("c"));
        assertTrue(q.containsKey("d"));
        assertTrue(null != q.remove("c"));      // a b d
        assertTrue(q.containsKey("a"));
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("d"));
        q.put("f","a");                         // a b d f
        assertTrue(null != q.remove("d"));      // a b f
        assertTrue(q.containsKey("a"));
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("f"));
    }

    /* Remove Test 3
    *  Checks that remove works correctly on root nodes
    *  when the node has only 1 or 0 children on either side. */
    @Test
    public void testRemoveRootEdge() {
        BSTMap rightChild = new BSTMap();
        rightChild.put('A', 1);
        rightChild.put('B', 2);
        Integer result = (Integer) rightChild.remove('A');
        assertTrue(result.equals(new Integer(1)));
        for (int i = 0; i < 10; i++) {
            rightChild.put((char) ('C'+i), 3+i);
        }
        rightChild.put('A', 100);
        assertTrue(((Integer) rightChild.remove('D')).equals(new Integer(4)));
        assertTrue(((Integer) rightChild.remove('G')).equals(new Integer(7)));
        assertTrue(((Integer) rightChild.remove('A')).equals(new Integer(100)));
        assertTrue(rightChild.size()==9);

        BSTMap leftChild = new BSTMap();
        leftChild.put('B', 1);
        leftChild.put('A', 2);
        assertTrue(((Integer) leftChild.remove('B')).equals(1));
        assertEquals(1, leftChild.size());
        assertEquals(null, leftChild.get('B'));

        BSTMap noChild = new BSTMap();
        noChild.put('Z', 15);
        assertTrue(((Integer) noChild.remove('Z')).equals(15));
        assertEquals(0, noChild.size());
        assertEquals(null, noChild.get('Z'));
    }

    /* Remove test (basics) */
    @Test
    public void testRemoveBasics() {
        BSTMap<Integer, String> bst = new BSTMap<>();
        bst.put(1, "a");

        assertEquals(null, bst.remove(3));
        assertEquals("a", bst.remove(1));
        assertEquals(0, bst.size());
        assertFalse(bst.containsKey(1));

        bst.put(2, "b");
        bst.put(1, "a");
        bst.put(4, "d");
        bst.put(3, "c");
        bst.put(5, "e");

        assertEquals("b", bst.remove(2));
        assertEquals("d", bst.remove(4));
        assertEquals(3, bst.size());
        assertFalse(bst.containsKey(2));
        assertFalse(bst.containsKey(4));
        assertTrue(bst.containsKey(1));
        assertTrue(bst.containsKey(3));
        assertTrue(bst.containsKey(5));
    }

    /* Iterator test 1
     * helper
     */
    @Test
    public void testAllIsNull() {
        BSTMap<Integer, String> bst = new BSTMap<>();
        assertTrue(bst.allIsNull());

        bst.put(2, "b");
        bst.put(1, "a");
        bst.put(4, "d");
        bst.put(3, "c");
        bst.put(5, "e");

        bst.remove(3);
        bst.remove(4);
        bst.remove(5);
        assertFalse(bst.allIsNull());

        bst.remove(1);
        bst.remove(2);
        assertTrue(bst.allIsNull());
    }

    /* Iterator test 2
     * the real deal
     */
    @Test
    public void testIterator() {
        BSTMap<Integer, String> bst = new BSTMap<>();
        bst.put(2, "b");
        bst.put(4, "d");
        bst.put(5, "e");
        bst.put(3, "c");
        bst.put(1, "a");

        Iterator<Integer> iter = bst.iterator();
        assertTrue(iter.hasNext());
        assertEquals(1, (int) iter.next());
        assertEquals(2, (int) iter.next());
        assertEquals(3, (int) iter.next());
        assertEquals(4, (int) iter.next());
        assertTrue(iter.hasNext());

        assertEquals(5, bst.size());

        assertEquals(5, (int) iter.next());
        assertFalse(iter.hasNext());
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestBSTMapExtra.class);
    }
}
