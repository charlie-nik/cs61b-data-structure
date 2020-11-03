package byow.Core;

import byow.BuildingBlock.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Primary class for random world generation. The algorithm is as follows. First, builds a
 * starter room. Second, builds a hallway coming out of starter-room, then another room at the end
 * of the first hallway. Third, randomly selects an existing room, builds a hallway out of it,
 * and a room at the end of the hallway. Repeats step 3 until the total number of occupied tiles
 * reaches LOAD_FACTOR.
 */
public class WorldGenerator {
    private final int WIDTH;
    private final int HEIGHT;
    private final Random RANDOM;

    public final TETile[][] WORLD;
    private final double LOAD_FACTOR = 0.22;
    private int totalNumOfTiles;

    /**
     * Initializes a TETile[][] board. Fills every tile with NOTHING, and then replaces tiles
     * with FLOOR and WALL according to the world generation algorithm.
     */
    public WorldGenerator(int width, int height, Random random) {
        WIDTH = width;
        HEIGHT = height;
        RANDOM = random;
        WORLD = new TETile[width][height];
        totalNumOfTiles = 0;

        fillBackground();
        generateWorld();
    }

    /**
     * Fills every tile of the world with NOTHING.
     */
    private void fillBackground() {
        for (int i = 0; i < WORLD.length; i++) {
            for (int j = 0; j < WORLD[0].length; j++) {
                WORLD[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * The primary method for world generation. First, builds a starter room. Second, repeats
     * the following steps until the number of occupied tiles reaches LOAD_FACTOR:
     *      A. randomly choose an existing room
     *      B. try building a hallway out of it
     *          i.  if succeeds, there's a 50% chance of choosing to try building a hallway turn
     *          ii. if succeeds, try building a room at the end of the hallway
     */
    private void generateWorld() {
        buildRoom(null);
        while (load() < LOAD_FACTOR) {
            int randomIndex = RANDOM.nextInt(Area.ROOMS.size());
            Room room = Area.ROOMS.get(randomIndex);
            Hallway hallway = buildHallway(room);
            if (hallway != null) {
                hallway = buildTurn(hallway);
                buildRoom(hallway);
            }
        }
    }

    /**
     * Tries building a new room. In the case of starter room, tries for infinite times until
     * succeed. In the case of regular rooms, tries for 6 attempts - 6 but not less because
     * construction success rate can be low.
     */
    private Room buildRoom(Hallway hallway) {
        if (hallway == null) {
            return (Room) multipleAttempts(null, hallway, Integer.MAX_VALUE);
        } else {
            return (Room) multipleAttempts(null, hallway, 6);
        }
    }

    /**
     * Tries building a new hallway within 3 attempts.
     */
    private Hallway buildHallway(Room room) {
        return (Hallway) multipleAttempts(room, null, 3);
    }

    /**
     * Tries building an area instance within the given number of attempts. If an instance is
     * successfully created, increases the total number of occupied tiles.
     */
    private Area multipleAttempts(Room room, Hallway hallway, int numAttempts) {
        while (numAttempts > 0) {
            Area area = room == null ? new Room(WORLD, hallway, RANDOM) :
                                       new Hallway(WORLD, room, RANDOM);
            if (area.isInstanceCreated()) {
                totalNumOfTiles += area.numOfTiles();
                return area;
            }
            numAttempts--;
        }
        return null;
    }

    /**
     * There's a 50% chance of choosing to build a hallway turn (in one attempt, since
     * construction success rate is very high). If a valid hallway turn is built, returns it.
     * Otherwise, returns the original hallway.
     */
    private Hallway buildTurn(Hallway hallway) {
        boolean buildTurn = RandomUtils.bernoulli(RANDOM, 0.5);
        if (buildTurn) {
            hallway = buildMultipleTurns(hallway);
        }
        return hallway;
    }

    /**
     * Randomly determines how many turns are to be constructed: at most 2, at least 1. If hallway
     * turn(s) is created, returns it, otherwise returns the original hallway.
     */
    private Hallway buildMultipleTurns(Hallway hallway) {
        int numOfTurns = RANDOM.nextInt(2) + 1;
        for (int i = 0; i < numOfTurns; i++) {
            Hallway turn = Hallway.hallwayTurn(WORLD, hallway);
            if (turn.isInstanceCreated()) {
                totalNumOfTiles += turn.numOfTiles() - 1;
                hallway = turn;
            }
        }
        return hallway;
    }

    /**
     * Returns the percentage of occupied tiles against the total number of tiles.
     */
    private double load() {
        return (float) totalNumOfTiles / (WIDTH * HEIGHT);
    }

    /**
     * For testing.
     */
    public static void main(String[] args) {
        TERenderer teRenderer = new TERenderer();
        teRenderer.initialize(80, 30);

        long seed = 818;
        Random r = new Random(seed);
        WorldGenerator wg = new WorldGenerator(80, 30, r);

        teRenderer.renderFrame(wg.WORLD);
    }

}
