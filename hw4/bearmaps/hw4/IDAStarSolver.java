package bearmaps.hw4;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.Stack;

/**
 * It seems that IDA* is not so much an A* or Dijkstra as a DFS...
 */
public class IDAStarSolver<Vertex> extends AStarSolver<Vertex> {

    private Stopwatch sw;
    private Stack<Vertex> paths;

    public IDAStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        super(input, start, end, timeout);
    }

    @Override
    protected void a_star() {
        sw = new Stopwatch();
        ida_star();
        totalTime = sw.elapsedTime();
    }

    private void ida_star() {
        paths = new Stack<>();
        paths.push(s);

        distTo.put(s, 0.0);
        double threshold = priority(s);

        while (true) {
            Vertex v = paths.peek();
            double distance = ids(v, threshold);
            if (outcome != null) {
                // Target found or timeout
                return;
            } else if (distance == Double.MAX_VALUE) {
                // Target not found and no more states to visit
                outcome = SolverOutcome.UNSOLVABLE;
                return;
            } else {
                // if not found, ids returns the next-bigger threshold;
                threshold = distance;
            }
        }
    }

    /**
     * Performs DFS up to a depth where a threshold is reached,
     * as opposed to iterative-deepening DFS which stops at a fixed depth.
     * Returns -1 if target found, -2 if timeout.
     * (IDS => Iterative Deepening Search)
     */
    // Iterative Deepening Search (IDS)
    private double ids(Vertex v, double threshold) {
        /*if (sw.elapsedTime() > timeout) {
            outcome = SolverOutcome.TIMEOUT;
            return -1;
        }*/
        if (v.equals(t)) {
            outcome = SolverOutcome.SOLVED;
            return -1;
        }

        double cost = priority(v);  // f(n) = g(n) + h(n)
        if (cost > threshold) {
            // Threshold breached
            return cost;
        }

        // Explore all vertexes rooted at this vertex until threshold is breached,
        // then update threshold to the new minimum (next-smallest),
        // which is basically the priority of the next vertex to be explored.
        double min = Double.MAX_VALUE;
        Vertex next = null;
        for (WeightedEdge<Vertex> e : graph.neighbors(v)) {
            Vertex w = e.to();
            if (!paths.contains(w)) {
                double newDist = distTo.get(v) + e.weight();
                if (!distTo.containsKey(w) || newDist < distTo.get(w)) {
                    distTo.put(w, newDist);
                    edgeTo.put(w, v);
                }
                double t = ids(w, threshold);
                if (t < 0) {
                    return t;   // target found or timeout
                } else if (t < min) {
                    min = t;
                    next = w;
                }
            }
        }
        if (next != null) {
            paths.push(next);
        }
        return min;
    }

    @Override
    public int numStatesExplored() {
        return paths.size();
    }
}
