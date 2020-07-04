package bearmaps.hw4.rubikscube;

import bearmaps.hw4.AStarSolver;
import bearmaps.hw4.LazySolver;
import bearmaps.hw4.ShortestPathsSolver;
import bearmaps.hw4.SolutionPrinter;

public class DemoRubiksCubeSolution {

    public static void main(String[] args) {
        Cube start = Cube.readCube("BasicCube1.txt");
        Cube goal = Cube.solved();

        CubeGraph graph = new CubeGraph();

        ShortestPathsSolver<Cube> solver = new AStarSolver<>(graph, start, goal, 20);
        SolutionPrinter.summarizeSolution(solver, "\n");
    }
}
