package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class WorldGenerator {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private int numOccupied;
    // private final TETile[][] WORLD;

    /*
    public WorldGenerator(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        numOccupied = 0;
        WORLD = new TETile[w][h];
    }
     */

    public static void fillBackground(TETile[][] world) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static void main(String[] args) {
        TERenderer teRenderer = new TERenderer();
        teRenderer.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillBackground(world);

        for (int N = 0; N < 20; N++) {
            // Areaaa.addRoom(world);
        }
        for (int N = 0; N < 40; N++) {
            // Areaaa.addHallway(world);
        }

        for (int i = 0; i < 7; i++) {
            world[20][10 + i] = Tileset.GRASS;
        }
        Room r = new Room(20, 16, null);
        r.addToWorld(world);
        new Hallway(r).addToWorld(world);

        teRenderer.renderFrame(world);
    }

}
