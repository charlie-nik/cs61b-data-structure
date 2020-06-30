import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTrieSet implements TrieSet61B {

    private class Node {

        private Character key;
        private boolean isKey;
        private Map<Character, Node> children;

        Node(Character key, boolean isKey) {
            this.key = key;
            this.isKey = isKey;
            children = new HashMap<>();
        }
    }

    private Node root;
    private int height;

    /* Constructor. */
    public MyTrieSet() {
        root = new Node(null, false);
        height = 0;
    }

    /* Clears all items out of Trie. */
    @Override
    public void clear() {
        root.children.clear();
        height = 0;
    }

    /* Returns true if the Trie contains KEY, false otherwise. */
    @Override
    public boolean contains(String key) {
        if (key == null || key.length() < 1) throw new IllegalArgumentException("Argument to contains() is null");
        Node n = find(key);
        return n != null && n.isKey;
    }

    /* Inserts string KEY into Trie.
     * Hug's solution - 机智！！*/
    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) return;
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!curr.children.containsKey(c)) {
                curr.children.put(c, new Node(c, false));
            }
            curr = curr.children.get(c);
        }
        curr.isKey = true;
        height = Math.max(height, key.length());
    }

    /* Returns a list of all words that start with PREFIX. */
    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> collected = new ArrayList<>();
        if (prefix.length() > height) return collected; // if prefix is longer than the longest word in Trie

        Node n = find(prefix);
        collect(n, collected, prefix);
        return collected;
    }

    // helper method for keysWithPrefix()
    private void collect(Node n, List<String> collected, String prefix) {
        if (n.isKey) collected.add(prefix);
        for (Character c : n.children.keySet()) {
            collect(n.children.get(c), collected, prefix + c);
        }
    }

    // helper method to find the target node for contains() and keysWithPrefix()
    private Node find(String key) {
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!curr.children.containsKey(c)) return null;
            curr = curr.children.get(c);
        }
        return curr;
    }

    @Override
    public String longestPrefixOf(String key) {
        if (key == null || key.length() < 1) return null;

        StringBuilder temp = new StringBuilder();
        String prefix = "";
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!curr.children.containsKey(c)) break;
            temp.append(c);
            curr = curr.children.get(c);
            if (curr.isKey) prefix = temp.toString();
        }
        return prefix;
    }
}
