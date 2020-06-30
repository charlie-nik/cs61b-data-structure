import java.util.*;

public class EnemyDFS {

    private final Graph graph;
    private final Map<String, Boolean> marked;
    private final List<Set<String>> forbidden;  // a list of each table's set of forbidden members

    public boolean isSeparable = true;

    public EnemyDFS(Graph g, int n) {
        // initialization
        graph = g;
        marked = new HashMap<>();
        forbidden = new ArrayList<>();
        for (String invitee : graph.labels()) { // runtime: O(V)
            marked.put(invitee, false);
            if (forbidden.size() < n) {
                forbidden.add(new HashSet<>());
            }
        }

        // dfs
        // runtime: O(V + E)
        for (String invitee : graph.labels()) {
            if (!marked.get(invitee)) {
                dfs(invitee);
            }
        }
    }

    private void dfs(String invitee) {
        if (isSeparable) {
            marked.put(invitee, true);
            int table = 0; // assign invitees to tables
            while (forbidden.get(table).contains(invitee)) { // runtime: O(1)
                if (++table == forbidden.size()) {
                    isSeparable = false;
                    return;
                }
            }
            forbidden.get(table).addAll(graph.neighbors(invitee)); // runtime: ~2E
            for (String enemy : graph.neighbors(invitee)) { // runtime: O(E)
                if (!marked.get(enemy)) {
                    dfs(enemy);
                }
            }
        }
    }
}
