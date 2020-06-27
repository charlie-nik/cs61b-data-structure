import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private int size;

        /* Creates a leaf node with the given KEY and VALUE. */
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            size = 1;
        }
    }

    /* Creates an empty map. */
    public BSTMap() {
    }

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
        } else {
            return n.value;
        }
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
            return new Node(key, value);
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = put(n.left, key, value);
        } else if (cmp > 0) {
            n.right = put(n.right, key, value);
        } else {
            n.value = value;
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
        keys.add(n.key);
        keys.addAll(keySet(n.left));
        keys.addAll(keySet(n.right));
        return keys;
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        V result = get(key);
        if (result != null) {
            root = remove(key, root);
        }
        return result;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        V result = get(key);
        if (result.equals(value)) {
            root = remove(key, root);
            return result;
        }
        return null;
    }

    /* Return node N with KEY deleted from it. */
    private Node remove(K key, Node n) {
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = remove(key, n.left);
        } else if (cmp > 0) {
            n.right = remove(key, n.right);
        } else {
            if (n.left == null) {
                return n.right;
            } else if (n.right == null) {
                return n.left;
            } else {
                Node temp = n;
                n = minNode(temp.right);
                n.right = removeMin(temp.right);
                n.left = temp.left;
            }
        }
        n.size = 1 + size(n.left) + size(n.right);
        return n;
    }

    /* Return the minimum node rooted at the given node. */
    private Node minNode(Node n) {
        if (n.left == null) {
            return n;
        }
        return minNode(n.left);
    }

    /* Return node N with its minimum node removed. */
    private Node removeMin(Node n) {
        if (n.left == null) {
            return n.right;
        }
        n.left = removeMin(n.left);
        n.size = 1 + size(n.left) + size(n.right);
        return n;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {
        Iterator<K> iter;
        BSTMapIterator() {
            List<K> keys = new ArrayList<>();
            nodeToArray(root, keys);
            iter = keys.iterator();
        }

        private void nodeToArray(Node n, List<K> keys) {
            if (n.left == null && n.right == null) {
                keys.add(n.key);
            } else if (n.left == null) {
                keys.add(n.key);
                nodeToArray(n.right, keys);
            } else if (n.right == null) {
                nodeToArray(n.left, keys);
                keys.add(n.key);
            } else {
                nodeToArray(n.left, keys);
                keys.add(n.key);
                nodeToArray(n.right, keys);
            }
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public K next() {
            return iter.next();
        }
    }

    /* Prints out the BSTMap in order of increasing Key. */
    public void printInOrder() {
        Iterator<K> iterator = new BSTMapIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
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
