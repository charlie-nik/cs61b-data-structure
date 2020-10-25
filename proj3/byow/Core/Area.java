package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.SpaceUtils.Position;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * An interface for rooms and hallways.
 *
 * The interface itself provides two default methods that support the following functionality:
 *
 *    - addToWorld(): draws the area instance onto the world
 *    - isValid(): checks the validity of the newly created instance, especially whether it
 *      overlaps with preexisting instances
 *
 */
public interface Area {
    Set<Area> AREAS = new HashSet<>();
    int WIDTH = 80;
    int HEIGHT = 30;

    Position position();
    int width();
    int height();
    int numOfTiles();


    //region Draw onto World
    //----------------------------------------------------------------------------------------
    /**
     * Adds the given area to the world.
     */
    default void addToWorld(TETile[][] world) {
        drawArea(world);
        drawWall(world);
    }

    private void drawArea(TETile[][] world) {
        // TETile tile = TETile.colorVariant(Tileset.MOUNTAIN, 64, 64, 64, new Random());
        TETile tile = Tileset.FLOOR;

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                int currX = position().getX() + x;
                int currY = position().getY() - y;
                world[currX][currY] = tile;
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
            world[x][y] = TETile.colorVariant(Tileset.WALL, 32, 32, 32, new Random());
        }
    }
    //endregion


    //region Check Validity: Overlap
    //----------------------------------------------------------------------------------------
    /**
     * Returns true if a valid area is generated, namely if it is substantial and doesn't
     * overlap with other existing ones except the place it comes from, with which it
     * necessarily overlaps.
     */
    default boolean isValid(Area origin) {
        return width() > 0 && height() > 0 && !hasOverlap(origin);
    }

    /**
     * Returns true if the target area overlaps with existing ones, false otherwise.
     */
    private boolean hasOverlap(Area origin) {
        for (Area curr : AREAS) {
            if (!curr.equals(origin)) {
                if (xOverlap(this, curr) && yOverlap(this, curr)) {
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
        return ((s1 <= s2) && (e1 >= s2)) || ((s1 <= e2) && (e1 >= e2));
    }

    /**
     * Returns true if two areas overlap on the y-axis.
     */
    private static boolean yOverlap(Area a1, Area a2) {
        int s1 = a1.position().getY() + 1;
        int e1 = s1 - a1.height() - 1;
        int s2 = a2.position().getY() + 1;
        int e2 = s2 - a2.height() - 1;
        return ((s1 >= s2) && (e1 <= s2)) || ((s1 >= e2) && (e1 <= e2));
    }
    //endregion
}
