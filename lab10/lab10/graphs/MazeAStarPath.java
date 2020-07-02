package lab10.graphs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private final int s;
    private final int t;
    private final PriorityQueue<Node> pq;
    private final HashMap<Integer, Node> map; // for changing priority


    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;

        Comparator<Node> comparator = Comparator.comparingInt((Node n) -> n.priority);
        pq = new PriorityQueue<>(comparator);
        map = new HashMap<>();
    }

    /** Estimate of the distance from v to the target.
     *  using Manhattan distance (or known as Taxicab metric) for calculation.
     */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search of the maze starting at the source. */
    private void astar() { // overall runtime: O((V + V + E) * log V) = O(E log V)
        addNode(s);

        while (!pq.isEmpty()) { // runtime: O(V)
            int v = pq.remove().item;   // runtime: O(log V)
            marked[v] = true;
            announce();

            if (v == t) {
                return;
            }

            for (int w : maze.adj(v)) { // runtime: O(E)
                int relaxed = distTo[v] + 1;
                if (!marked[w] && relaxed < distTo[w]) {
                    distTo[w] = relaxed;
                    edgeTo[w] = v;
                    if (map.containsKey(w)) {   // runtime: O(1)
                        pq.remove(map.get(w));  // runtime: O(log V)
                    }
                    addNode(w);
                }
            }
        }
    }

    private class Node {
        private final int item;
        private final int priority;
        Node(int v) {
            this.item = v;
            this.priority = distTo[v] + h(v);
        }
    }

    // add to both queue and map
    // runtime: O(log V)
    private void addNode(int v) {
        Node n = new Node(v);
        pq.add(n);
        map.put(v, n);
    }

    @Override
    public void solve() {
        astar();
    }

}
