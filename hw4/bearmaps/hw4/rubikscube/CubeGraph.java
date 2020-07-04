package bearmaps.hw4.rubikscube;

import bearmaps.hw4.AStarGraph;
import bearmaps.hw4.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

public class CubeGraph implements AStarGraph<Cube> {

    @Override
    public List<WeightedEdge<Cube>> neighbors(Cube v) {
        List<Cube> neighbors = v.neighbors();
        List<WeightedEdge<Cube>> neighborEdges = new ArrayList<>();
        for (Cube c : neighbors) {
            neighborEdges.add(new WeightedEdge<>(v, c, 1));
        }
        return neighborEdges;
    }

    @Override
    public double estimatedDistanceToGoal(Cube s, Cube goal) {
        return 0;
    }
}
