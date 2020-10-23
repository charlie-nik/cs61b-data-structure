package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 * Inspired by @source Josh Hug's implementation.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        TERenderer teRenderer = new TERenderer();
        teRenderer.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillBackground(world, Tileset.NOTHING);

        Position root = new Position(23, 5);
        addHexTesselation(world, root, 4);

        teRenderer.renderFrame(world);
    }



    // draw single hex //

    /**
     * Computes the width of row ROW for a size SIZE hexagon.
     */
    public static int hexRowWidth(int size, int row) {
        int effectiveRow = row;
        if (row >= size) {
            effectiveRow = 2 * size - row - 1;
        }
        return size + 2 * effectiveRow;
    }

    /**
     * Computes the x-coordinate of the leftmost tile in the ROW-th row of
     * a hexagon of size SIZE, assuming that the bottom row has an x-coordinate
     * of zero.
     */
    public static int hexRowOffset (int size, int row) {
        int effectiveRow = row;
        if (row >= size) {
            effectiveRow = 2 * size - row - 1;
        }
        return -effectiveRow;
    }

    /**
     * Adds a hexagon of size SIZE at position P in the WORLD.
     */
    public static void addHexagon(TETile[][] world, Position p, int size, TETile tile) {
        if (size < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        for (int y = 0; y < size * 2; y++) {
            int xRowStart = p.x + hexRowOffset(size, y);
            int rowWidth = hexRowWidth(size, y);

            for (int x = 0; x < rowWidth; x++) {
                world[xRowStart + x][p.y + y] =
                        TETile.colorVariant(tile, 32, 32, 32, RANDOM);
            }
        }
    }



    // draw tesselation of hexes //

    /**
     * Computes the number of hexagons contained in the N-th column.
     */
    public static int columnSize(int n) {
        int effectiveN = n;
        if (n >= 3) {
            effectiveN = 3 - (n - 3) - 2;
        }
        return effectiveN + 3;
    }

    /**
     * Computes the x-offset and y-offset of the N-th column that contains hexagons of size S.
     * Assuming the origin position is (0, 0).
     */
    public static Position columnXYOffset(int s, int n) {
        int effectiveN = n;
        if (n >= 3) {
            effectiveN = 3 - (n - 3) - 2;
        }
        int yOffset = (2 - effectiveN) * s;
        int xOffset = -(s + (s - 1)) * (2 - n);
        return new Position(xOffset, yOffset);
    }

    /**
     * Adds a column that contains M hexagons of size S to position P of the WORLD.
     * Position P designates the leftmost tile at the bottom row of the column.
     */
    public static void addColumnOfHexes(TETile[][] world, Position p, int s, int m) {
        for (int i = 0; i < m; i++) {
            int yCoord = p.y + 2 * s * i;
            addHexagon(world, new Position(p.x, yCoord), s, randomTile());
        }
    }

    /**
     * Adds 5 columns of size S hexagons to the WORLD to form a size-3 tesselation.
     */
    public static void addHexTesselation(TETile[][] world, Position p, int s) {
        for (int i = 0; i < 5; i++) {
            int xCoord = p.x + columnXYOffset(s, i).x;
            int yCoord = p.y + columnXYOffset(s, i).y;
            addColumnOfHexes(world, new Position(xCoord, yCoord), s, columnSize(i));
        }
    }

    private static class Position {
        private final int x, y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        return switch (tileNum) {
            case 0 -> Tileset.TREE;
            case 1 -> Tileset.GRASS;
            case 2 -> Tileset.FLOWER;
            case 3 -> Tileset.SAND;
            default -> Tileset.MOUNTAIN; // case 4
        };
    }

    public static void fillBackground(TETile[][] world, TETile tile) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = tile;
            }
        }
    }



    // testing //

    @Test
    public void testHexRowWidth() {
        assertEquals(3, hexRowWidth(3, 5));
        assertEquals(5, hexRowWidth(3, 4));
        assertEquals(7, hexRowWidth(3, 3));
        assertEquals(7, hexRowWidth(3, 2));
        assertEquals(5, hexRowWidth(3, 1));
        assertEquals(3, hexRowWidth(3, 0));
        assertEquals(2, hexRowWidth(2, 0));
        assertEquals(4, hexRowWidth(2, 1));
        assertEquals(4, hexRowWidth(2, 2));
        assertEquals(2, hexRowWidth(2, 3));
    }

    @Test
    public void testHexRowOffset() {
        assertEquals(0, hexRowOffset(3, 5));
        assertEquals(-1, hexRowOffset(3, 4));
        assertEquals(-2, hexRowOffset(3, 3));
        assertEquals(-2, hexRowOffset(3, 2));
        assertEquals(-1, hexRowOffset(3, 1));
        assertEquals(0, hexRowOffset(3, 0));
        assertEquals(0, hexRowOffset(2, 0));
        assertEquals(-1, hexRowOffset(2, 1));
        assertEquals(-1, hexRowOffset(2, 2));
        assertEquals(0, hexRowOffset(2, 3));
    }

    @Test
    public void testColumnSize() {
        assertEquals(3, columnSize(0));
        assertEquals(4, columnSize(1));
        assertEquals(5, columnSize(2));
        assertEquals(4, columnSize(3));
        assertEquals(3, columnSize(4));
    }


    /*
    // The following two methods are later combined into one (HexWorld.columnXYOffset),
    // thus their corresponding tests are now obsolete.

    @Test
    public void testColumnYOffset() {
        assertEquals(0, columnYOffset(3, 2));
        assertEquals(0, columnYOffset(5, 2));
        assertEquals(4, columnYOffset(4, 1));
        assertEquals(5, columnYOffset(5, 1));
        assertEquals(5, columnYOffset(5, 3));
        assertEquals(2, columnYOffset(2, 3));
        assertEquals(6, columnYOffset(3, 0));
        assertEquals(8, columnYOffset(4, 0));
        assertEquals(4, columnYOffset(2, 4));
        assertEquals(10, columnYOffset(5, 4));
    }

    @Test
    public void testColumnXOffset() {
        assertEquals(0, columnXOffset(3, 2));
        assertEquals(0, columnXOffset(5, 2));
        assertEquals(-7, columnXOffset(4, 1));
        assertEquals(-9, columnXOffset(5, 1));
        assertEquals(9, columnXOffset(5, 3));
        assertEquals(3, columnXOffset(2, 3));
        assertEquals(-10, columnXOffset(3, 0));
        assertEquals(-14, columnXOffset(4, 0));
        assertEquals(6, columnXOffset(2, 4));
        assertEquals(18, columnXOffset(5, 4));
    }
     */

}
