package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

/**
 * Note:
 * I changed most instance members in this class from "private" to "protected"
 * in order to extend the AStarSolver class in the ADAStarSolver class.
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    protected final AStarGraph<Vertex> graph;
    protected final Vertex s;
    protected final Vertex t;
    protected final double timeout;

    protected final HashSet<Vertex> marked;
    protected final HashMap<Vertex, Double> distTo;
    protected final HashMap<Vertex, Vertex> edgeTo;
    protected ArrayHeapMinPQ<Vertex> pq;

    protected SolverOutcome outcome;
    protected double totalTime;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        graph = input;
        s = start;
        t = end;
        this.timeout = timeout;

        marked = new HashSet<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        a_star();
    }

    protected void a_star() {
        Stopwatch sw = new Stopwatch();

        pq = new ArrayHeapMinPQ<>();
        distTo.put(s, 0.0);
        pq.add(s, priority(s));

        while (!pq.isEmpty()) {
            Vertex v = pq.removeSmallest();
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                break;
            }
            if (v.equals(t)) {
                outcome = SolverOutcome.SOLVED;
                break;
            }
            marked.add(v);
            for (WeightedEdge<Vertex> edge: graph.neighbors(v)) {
                if (!marked.contains(edge.to())) {
                    relax(edge);
                }
            }
        }
        totalTime = sw.elapsedTime();
        outcome = outcome == null ? SolverOutcome.UNSOLVABLE : outcome;
    }

    protected double priority(Vertex v) {
        return distTo.get(v) + graph.estimatedDistanceToGoal(v, t);
    }

    protected void relax(WeightedEdge<Vertex> e) {
        Vertex from = e.from();
        Vertex to = e.to();
        double newDist = distTo.get(from) + e.weight();
        if (!pq.contains(to) || newDist < distTo.get(to)) {
            distTo.put(to, newDist);
            edgeTo.put(to, from);
            if (!pq.contains(to)) {
                pq.add(to, priority(to));
            } else {
                pq.changePriority(to, priority(to));
            }
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        List<Vertex> solution = new LinkedList<>();
        if (outcome == SolverOutcome.SOLVED) {
            Vertex curr = t;
            while (!curr.equals(s)) {
                solution.add(curr);
                curr = edgeTo.get(curr);
            }
            solution.add(curr);
            Collections.reverse(solution);
        }
        return solution;
    }

    @Override
    public double solutionWeight() {
        if (outcome == SolverOutcome.SOLVED) {
            return distTo.get(t);
        }
        return 0;
    }

    @Override
    public int numStatesExplored() {
        return marked.size();
    }

    @Override
    public double explorationTime() {
        return totalTime;
    }
}
