import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private boolean isNull;
        private int size;

        /* Creates a leaf node with the given KEY and VALUE. */
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            isNull = false;
            size = 1;
        }
    }

    /* Creates an empty map. */
    public BSTMap() { }

    /* Creates a single-node map with the given KEY and VALUE. */
    public BSTMap(K key, V value) {
        root = new Node(key, value);
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    /* Helper function for get(key). */
    private V get(Node n, K key) {
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            return get(n.left, key);
        } else if (cmp > 0) {
            return get(n.right, key);
        } else if (n.isNull) {
            return null;
        }
        return n.value;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    /* Helper function for size(). */
    private int size(Node n) {
        if (n == null) {
            return 0;
        }
        return n.size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    /* Helper function for put(key, value). */
    private Node put(Node n, K key, V value) {
        if (n == null) {
            n = new Node(key, value);
            return n;
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = put(n.left, key, value);
        } else if (cmp > 0) {
            n.right = put(n.right, key, value);
        } else {
            n.value = value;
            n.isNull = false;
        }
        n.size = 1 + size(n.left) + size(n.right);
        return n;
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keySet(root);
    }

    /* Helper function for keySet(). */
    private Set<K> keySet(Node n) {
        Set<K> keys = new HashSet<>();
        if (n == null) {
            return keys;
        }
        if (!n.isNull) {
            keys.add(n.key);
        }
        keys.addAll(keySet(n.left));
        keys.addAll(keySet(n.right));
        return keys;
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        return remove(key, null, root);
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        return remove(key, value, root);
    }

    /* Helper function for remove. */
    private V remove(K key, V value, Node n) {
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp == 0) {
            if (value != null && !n.value.equals(value)) {
                return null;
            }
            V result = n.value;
            root.size -= 1;
            n.isNull = true;
            return result;
        } else if (cmp < 0) {
            return remove(key, value, n.left);
        } else {
            return remove(key, value, n.right);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    /* Returns true if all nodes rooted at N are null. */
    public boolean allIsNull() {
        return allIsNull(root);
    }

    /* Helper function for allIsNull(). */
    private boolean allIsNull(Node n) {
        if (n == null) {
            return true;
        }
        return n.isNull && allIsNull(n.right) && allIsNull(n.left);
    }

    private class BSTMapIterator implements Iterator<K> {
        int count;
        Node temp;
        BSTMapIterator() {
            count = 0;
            temp = root;
        }

        @Override
        public boolean hasNext() {
            return count < size(root);
        }

        @Override
        public K next() {
            return next(temp);
        }

        /* helper function for next(). */
        private K next(Node n) {
            if (!hasNext()) {
                throw new RuntimeException("End of iteration");
            }

            if (!allIsNull(n.left)) {
                return next(n.left);
            } else if (n.isNull && !allIsNull(n.right)) {
                return next(n.right);
            } else {
                K result = n.key;
                n.isNull = true;
                count += 1;
                return result;
            }
        }
    }

    /* Prints out the BSTMap in order of increasing Key. */
    public void printInOrder() {
        Iterator<K> iterator = new BSTMapIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /* A very elegant alternative to printInOrder(). */
    public void printInOrderAlt(Node n) {
        if (n.left == null && n.right == null) {
            printNode(n);
        } else if (n.left == null) {
            printNode(n);
            printInOrderAlt(n.right);
        } else if (n.right == null) {
            printInOrderAlt(n.left);
            printNode(n);
        } else {
            printInOrderAlt(n.left);
            printNode(n);
            printInOrderAlt(n.right);
        }
    }

    /* Helper function to the elegant alternative above. */
    private void printNode(Node n) {
        System.out.println(n.key + ": " + n.value);
    }

    /* testing. */
    public static void main(String[] args) {
        BSTMap<Integer, String> bst = new BSTMap<>();
        bst.put(2, "b");
        bst.put(4, "d");
        bst.put(5, "e");
        bst.put(3, "c");
        bst.put(1, "a");

        bst.printInOrder();
    }

}
