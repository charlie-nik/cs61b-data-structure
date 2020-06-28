import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V> {

    private class Pair {
        K key;
        V value;
        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int INIT_CAPACITY = 16;
    private final double loadFactor;
    private int size;                               // number of items in map
    private int capacity;                           // length of chains
    private LinkedList<Pair>[] chains;              // array of external chaining

    /* Creates an empty map. */
    public MyHashMap() {
        size = 0;
        capacity = INIT_CAPACITY;
        loadFactor = 0.75;
        initialize();
    }

    /* Creates an empty map with the specified initial capacity. */
    public MyHashMap(int initialSize) {
        size = 0;
        capacity = initialSize;
        loadFactor = 0.75;
        initialize();
    }

    /* Creates an empty map with the specified initial capacity
     * and specified load factor. */
    public MyHashMap(int initialSize, double loadFactor) {
        size = 0;
        capacity = initialSize;
        this.loadFactor = loadFactor;
        initialize();
    }

    /* Creates an array of chains. */
    private void initialize() {
        chains = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            chains[i] = new LinkedList<>();
        }
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            chains[i].clear();
        }
        size = 0;
    }

    /* Returns a hash value between 0 and capacity - 1. */
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    /* Resizes the map to the specified capacity. */
    private void resize(int capacity) {
        MyHashMap<K, V> temp = new MyHashMap<>(capacity, loadFactor);
        for (int i = 0; i < this.capacity; i++) {
            for (Pair p : chains[i]) {
                temp.put(p.key, p.value);
            }
        }
        this.size = temp.size;
        this.capacity = temp.capacity;
        this.chains = temp.chains;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new IllegalArgumentException("argument to containsKey() is null");
        return get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. */
    @Override
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        int index = hash(key);
        for (Pair p : chains[index]) {
            if (p.key.equals(key)) {
                return p.value;
            }
        }
        return null;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (value == null) {
            remove(key);
            return;
        }
        int index = hash(key);
        for (Pair p : chains[index]) {
            if (p.key.equals(key)) {
                p.value = value;
                return;
            }
        }
        chains[index].add(new Pair(key, value));
        size += 1;
        // double table size if load exceeds load factor
        if ((float) size / capacity > loadFactor) {
            resize(capacity * 2);
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (int i = 0; i < capacity; i++) {
            for (Pair p : chains[i]) {
                keys.add(p.key);
            }
        }
        return keys;
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("argument to remove() is null");
        int index = hash(key);
        for (Pair p : chains[index]) {
            if (p.key.equals(key)) {
                V removed = p.value;
                chains[index].remove(p);
                size -= 1;
                return removed;
            }
        }
        return null;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException. */
    @Override
    public V remove(K key, V value) {
        if (key == null) throw new IllegalArgumentException("first argument to remove() is null");
        int index = hash(key);
        for (Pair p : chains[index]) {
            if (p.key.equals(key) && p.value.equals(value)) {
                V removed = p.value;
                chains[index].remove(p);
                size -= 1;
                return removed;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
