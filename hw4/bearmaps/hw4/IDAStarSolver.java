package bearmaps.hw4;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.Stack;

/**
 * IDA*: a variant of iterative deepening depth-first search (IDDFS).
 * IDA* is a memory-constrained version of A*.
 * But unlike A*, IDA* doesn't use dynamic programming,
 * and often explores the same nodes many times -> LESS SPACE, MORE TIME.
 * A heuristic is not necessary (P.S. an admissible heuristic always underestimates).
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
            double distance = ids(threshold);
            if (distance < 0) {
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
     * IDS = Iterative Deepening Search
     * It performs DFS up to a depth where a threshold is reached,
     * as opposed to iterative-deepening DFS which stops at a fixed depth.
     * When the threshold is breached, it is updated and the algorithm starts all over again
     * from the root down to the new depth.
     */
    private double ids(double threshold) {
        Vertex v = paths.peek();
        if (sw.elapsedTime() > timeout) {
            outcome = SolverOutcome.TIMEOUT;
            return -1;
        }
        if (v.equals(t)) {
            outcome = SolverOutcome.SOLVED;
            return -1;
        }

        double f = priority(v);  // f(n) = g(n) + h(n)
        if (f > threshold) {
            // Threshold breached
            return f;
        }

        // Explore all vertexes rooted at this vertex until threshold is breached,
        // then update threshold to the new minimum (next-smallest).
        double min = Double.MAX_VALUE;
        for (WeightedEdge<Vertex> e : graph.neighbors(v)) {
            Vertex w = e.to();
            if (!paths.contains(w)) {
                paths.push(w);
                double g = distTo.get(v) + e.weight();  // g(n) = g(n.parent) + cost(n.parent, n)
                if (!distTo.containsKey(w) || g < distTo.get(w)) {
                    distTo.put(w, g);
                    edgeTo.put(w, v);
                }
                double t = ids(threshold);
                if (t < 0) {
                    return t;   // target found or timeout
                } else if (t < min) {
                    min = t;
                }
                paths.pop();
            }
        }
        return min;
    }

    // FOr IDA*, this method is not very informative
    @Override
    public int numStatesExplored() {
        return paths.size();
    }
}
