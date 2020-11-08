package byow.BuildingBlock;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * An interface for rooms and hallways. The interface provides three default methods that support
 * the following functionality: first, checks if an area instance is a valid instance; second,
 * draws an area instance onto the world; third, an enhanced Random.nextInt() method.
 */
public interface Area {
    // A list of all valid area instances, including both rooms and hallways
    List<Area> AREAS = new LinkedList<>();
    // A list of all valid room instances
    List<Room> ROOMS = new LinkedList<>();

    boolean isInstanceCreated();
    Position position();
    int width();
    int height();
    int numOfTiles();
    Random random();
    int attempt();

    /**
     * Returns true if the input area instance is a valid instance. Criteria of validity
     * include: first, it's substantial, namely that its width and height are greater than zero;
     * second, it doesn't overlap with existing area instances except its kins, with which it's
     * allowed to overlap. For a newly created Room instance, it has one kin, ie. the hallway that
     * leads to it. For a newly created Hallway instance, its kins include the room it comes from,
     * and all other hallways coming out of the same room in a direction other than its own.
     * Hallways coming in the same direction are regarded as strangers, not kins.
     */
    default boolean isValid(List<Area> kins) {
        return width() > 0 && height() > 0 && !hasOverlapExcept(kins);
    }

    /**
     * Returns true if the input area instance overlaps with existing areas that are not its kins.
     */
    private boolean hasOverlapExcept(List<Area> kins) {
        for (Area area : AREAS) {
            if (!kins.contains(area)) {
                if (xOverlap(this, area) && yOverlap(this, area)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if two areas overlap on the x-axis.
     */
    private static boolean xOverlap(Area a1, Area a2) {
        int s1 = a1.position().getX() - 1;
        int e1 = s1 + a1.width() + 1;
        int s2 = a2.position().getX() - 1;
        int e2 = s2 + a2.width() + 1;
        return ((s1 >= s2) && (s1 <= e2)) || ((e1 >= s2) && (e1 <= e2));
    }

    /**
     * Returns true if two areas overlap on the y-axis.
     */
    private static boolean yOverlap(Area a1, Area a2) {
        int s1 = a1.position().getY() + 1;
        int e1 = s1 - a1.height() - 1;
        int s2 = a2.position().getY() + 1;
        int e2 = s2 - a2.height() - 1;
        return ((s1 <= s2) && (s1 >= e2)) || ((e1 <= s2) && (e1 >= e2));
    }

    /**
     * Draws the given area to the world.
     */
    default void drawToWorld(TETile[][] world) {
        drawFloor(world);
        drawWall(world);
    }

    /**
     * Draws floor.
     */
    private void drawFloor(TETile[][] world) {
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                int currX = position().getX() + x;
                int currY = position().getY() - y;
                world[currX][currY] = Tileset.FLOOR;;
            }
        }
    }

    /**
     * Draws four walls around the area unless position is already occupied.
     */
    private void drawWall(TETile[][] world) {
        int xPos = position().getX();
        int yPos = position().getY();

        // top and bottom
        for (int x = -1; x <= width(); x++) {
            int currX = xPos + x;
            drawWallTile(world, currX, yPos + 1);
            drawWallTile(world, currX, yPos - height());
        }
        // left and right
        for (int y = -1; y <= height(); y++) {
            int currY = yPos - y;
            drawWallTile(world, xPos - 1, currY);
            drawWallTile(world, xPos + width(), currY);
        }
    }

    /**
     * If the given position isn't already occupied, draws a colorful wall tile.
     */
    private void drawWallTile(TETile[][] world, int x, int y) {
        if (world[x][y] == Tileset.NOTHING) {
            world[x][y] = TETile.colorVariant(Tileset.WALL, 32, 32, 32, random());
        }
    }

    /**
     * Makes sure the same Random (same seed) can generate different values for each attempt.
     * Without this enhancement, a failed attempt would continue to fail.
     */
    default int enhancedRandom(int bound) {
        int result = -1;
        for (int i = 0; i <= attempt(); i++) {
            result = random().nextInt(bound);
        }
        return result;
    }

}
