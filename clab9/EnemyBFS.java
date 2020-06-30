import java.util.*;

public class EnemyBFS {

    private final Graph graph;
    private final Map<String, Boolean> marked;
    private final List<Set<String>> forbidden;

    public boolean isSeparable = true;

    public EnemyBFS(Graph g, int n) {
        graph = g;
        marked = new HashMap<>();
        forbidden = new ArrayList<>();
        for (String guest : g.labels()) {
            marked.put(guest, false);
            if (forbidden.size() < n) {
                forbidden.add(new HashSet<>());
            }
        }

        // bfs
        for (String guest : g.labels()) {
            if (!marked.get(guest)) {
                bfs(guest);
            }
        }
    }

    private void bfs(String guest) {
        if (isSeparable) {
            Queue<String> fringe = new LinkedList<>();
            fringe.offer(guest);
            marked.put(guest, true);
            while (!fringe.isEmpty()) {
                String source = fringe.remove();
                int table = 0; // assign tables
                while (forbidden.get(table).contains(source)) {
                    if (++table == forbidden.size()) {
                        isSeparable = false;
                        return;
                    }
                }
                forbidden.get(table).addAll(graph.neighbors(source));
                for (String enemy : graph.neighbors(source)) {
                    if (!marked.get(enemy)) {
                        fringe.offer(enemy);
                        marked.put(enemy, true);
                    }
                }
            }
        }
    }

}
