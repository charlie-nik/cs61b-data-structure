package lab10.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int[] tempEdgeTo;
    private boolean cycleFound = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        tempEdgeTo = new int[maze.V()];
        dfs(0);
    }
    
    private void dfs(int v) {
        while (!cycleFound) {
            marked[v] = true;
            announce();
            for (int w : maze.adj(v)) {
                if (marked[w] && w != tempEdgeTo[v]) {
                    cycleFound = true;
                    drawCycle(v, w);
                    return;
                } else if (!marked[w]) {
                    tempEdgeTo[w] = v;
                    dfs(w);
                }
            }
        }
    }

    private void drawCycle(int v, int w) {
        edgeTo[w] = v;
        while (v != w) {
            edgeTo[v] = tempEdgeTo[v];
            v = tempEdgeTo[v];
        }
        announce();
    }
}
