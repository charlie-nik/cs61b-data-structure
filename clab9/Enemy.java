import java.util.*;

public class Enemy {

    private final Graph graph;
    private final Map<String, Boolean> marked;
    private final Map<String, Set<String>> enemyOf; // equivalent of edgeTo
    private final List<Set<String>> forbidden;  // a list of each table's set of forbidden members

    public boolean isSeparable = true;

    // for unit testing
    public Map<String, Set<String>> getEnemyOf() {
        return enemyOf;
    }
    public List<Set<String>> getForbidden() {
        return forbidden;
    }

    public Enemy(Graph g, int n) {
        // initialization
        graph = g;
        forbidden = new ArrayList<>();
        marked = new HashMap<>();
        enemyOf = new HashMap<>();
        for (String invitee : graph.labels()) { // runtime: O(V)
            if (forbidden.size() < n) {
                forbidden.add(new HashSet<>());
            }
            marked.put(invitee, false);
            enemyOf.put(invitee, new HashSet<>());
        }

        // dfs
        // runtime: O(V + E)
        for (String invitee : graph.labels()) {
            if (isSeparable && !marked.get(invitee)) {
                dfs(invitee);
            }
        }
    }

    // assign invitees to tables
    private void dfs(String invitee) {
        marked.put(invitee, true);
        int table = 0;
        while (forbidden.get(table).contains(invitee)) { // runtime: O(1)
            if (++table == forbidden.size()) {
                isSeparable = false;
                return;
            }
        }
        forbidden.get(table).addAll(graph.neighbors(invitee)); // runtime: ~2E
        for (String enemy : graph.neighbors(invitee)) { // runtime: O(E)
            if (isSeparable && !marked.get(enemy)) {
                dfs(enemy);
            }
        }
    }
}
