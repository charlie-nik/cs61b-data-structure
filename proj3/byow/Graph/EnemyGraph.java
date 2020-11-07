package byow.Graph;

import byow.BuildingBlock.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EnemyGraph {
    private final TETile[][] world;
    private final HashMap<Position, TETile> passableObjects;

    public EnemyGraph(TETile[][] world, HashMap<Position, TETile> passableObjects) {
        this.world = world;
        this.passableObjects = passableObjects;
    }

    public List<Edge> neighbors(Position pos) {
        List<Edge> neighbors = new LinkedList<>();
        addNeighbor(neighbors, pos, pos.east());
        addNeighbor(neighbors, pos, pos.west());
        addNeighbor(neighbors, pos, pos.north());
        addNeighbor(neighbors, pos, pos.south());
        return neighbors;
    }

    private void addNeighbor(List<Edge> neighbors, Position pos, Position neighbor) {
        TETile tile = world[neighbor.getX()][neighbor.getY()];
        if (tile == Tileset.FLOOR || tile == Tileset.AVATAR || passableObjects.containsValue(tile)) {
            neighbors.add(new Edge(pos, neighbor, 1));
        }
    }

    public int estimatedDistanceToGoal(Position pos, Position goal) {
        return Math.abs(pos.getX() - goal.getX()) + Math.abs(pos.getY() - goal.getY());
    }
}
