package lab10.graphs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
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

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        Comparator<Node> comparator = Comparator.comparingInt((Node n) -> n.priority);
        PriorityQueue<Node> pq = new PriorityQueue<>(comparator);
        HashMap<Integer, Node> map = new HashMap<>();
        pq.add(new Node(s));

        while (!pq.isEmpty()) {
            int v = pq.remove().item;
            marked[v] = true;
            announce();

            if (v == t) {
                return;
            }

            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    int relaxed = distTo[v] + 1;
                    if (relaxed < distTo[w]) {
                        distTo[w] = relaxed;
                        edgeTo[w] = v;
                        pq.add(new Node(w));
                    }
                }
            }
        }
    }

    private class Node {
        private int item;
        private int priority;
        Node(int v) {
            this.item = v;
            this.priority = distTo[v] + h(v);
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}
