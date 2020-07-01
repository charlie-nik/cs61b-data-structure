package lab10.graphs;

/**
 *  @author Josh Hug
 */
public class TrivialMazeExplorerDemo {
    public static void main(String[] args) {
        Maze maze = new Maze("lab10/graphs/maze.txt");
        TrivialMazeExplorer tme = new TrivialMazeExplorer(maze);
        tme.solve();
    }
}
