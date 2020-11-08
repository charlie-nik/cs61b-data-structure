package byow.Graph;

import byow.BuildingBlock.Position;

import java.util.*;

/**
 * Class for A* algorithm.
 */
public class AStarSolver {
    private final EnemyGraph graph;
    private final Position s, t;

    private final HashSet<Position> marked;
    private final HashMap<Position, Integer> distTo;
    private final HashMap<Position, Position> edgeTo;
    private final PriorityQueue<Position> pq;
    private boolean foundShortestPath = false;

    public AStarSolver(EnemyGraph graph, Position start, Position target) {
        this.graph = graph;
        this.s = start;
        this.t = target;

        marked = new HashSet<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        pq = new PriorityQueue<>(11, new PriorityComparator());

        aStar();
    }

    private void aStar() {
        distTo.put(s, 0);
        pq.add(s);

        while (!pq.isEmpty()) {
            Position v = pq.remove();
            if (v.equals(t)) {
                foundShortestPath = true;
                return;
            }
            marked.add(v);
            for (Edge edge : graph.neighbors(v)) {
                if (!marked.contains(edge.to())) {
                    relax(edge);
                }
            }
        }
    }

    private void relax(Edge e) {
        Position from = e.from();
        Position to = e.to();
        int newDistance = distTo.get(from) + e.weight();
        if (!pq.contains(to) || newDistance < distTo.get(to)) {
            distTo.put(to, newDistance);
            edgeTo.put(to, from);
            pq.remove(to);  // update priority
            pq.add(to);
        }
    }

    private class PriorityComparator implements Comparator<Position> {
        @Override
        public int compare(Position o1, Position o2) {
            return priority(o1) - priority(o2);
        }
        private int priority(Position pos) {
            return distTo.get(pos) + graph.estimatedDistanceToGoal(pos, t);
        }
    }

    public List<Position> shortestPath() {
        List<Position> path = new LinkedList<>();
        Position curr = t;
        while (!curr.equals(s)) {
            path.add(curr);
            curr = edgeTo.get(curr);
        }
        Collections.reverse(path);
        return path;
    }

    public boolean foundShortestPath() {
        return foundShortestPath;
    }
}
