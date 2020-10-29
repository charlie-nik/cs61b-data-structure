package byow.Graph;

import byow.BuildingBlock.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;

public class WorldGraph {
    private final TETile[][] WORLD;

    public WorldGraph(TETile[][] world) {
        WORLD = world;
    }

    public List<Edge> neighbors(Position pos) {
        List<Edge> neighbors = new LinkedList<>();
        addNeighbor(pos, neighbors, pos.getX(), pos.getY() + 1);
        addNeighbor(pos, neighbors, pos.getX(), pos.getY() - 1);
        addNeighbor(pos, neighbors, pos.getX() - 1, pos.getY());
        addNeighbor(pos, neighbors, pos.getX() + 1, pos.getY());
        return neighbors;
    }

    private void addNeighbor(Position pos, List<Edge> neighbors, int x, int y) {
        if (WORLD[x][y] == Tileset.FLOOR) {
            neighbors.add(new Edge(pos, new Position(x, y), 1));
        }
    }

    public int estimatedDistanceToGoal(Position pos, Position goal) {
        return Math.abs(pos.getX() - goal.getX()) + Math.abs(pos.getY() - goal.getY());
    }
}
