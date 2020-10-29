package byow.Core;

import byow.BuildingBlock.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Random;

public class WorldGenerator {
    private final int WIDTH;
    private final int HEIGHT;
    private final Random RANDOM;

    public final TETile[][] WORLD;
    private int totalNumOfTiles;


    public WorldGenerator(int width, int height, Random random) {
        WIDTH = width;
        HEIGHT = height;
        RANDOM = random;

        WORLD = new TETile[width][height];
        totalNumOfTiles = 0;

        fillBackground();
        generateWorld();
    }

    private void fillBackground() {
        for (int i = 0; i < WORLD.length; i++) {
            for (int j = 0; j < WORLD[0].length; j++) {
                WORLD[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void generateWorld() {
        // construct first room: no limit on maximum number of attempts, try until succeed
        Area starterRoom = severalAttempts(null, null, Integer.MAX_VALUE);

        // for (int i = 0; i < 10; i++)
        while (load() < 0.25) {
            int randomRoom = RANDOM.nextInt(Area.ROOMS.size());
            Room room = Area.ROOMS.get(randomRoom);
            int numHallways = room.hallways().size();
            int numFailedAttempts = room.hallwayAttempt - numHallways;
            int rand2 = randomRoom;
            if (numHallways > 2 || numFailedAttempts > 6) { // refined selection is ROOMS.size > 1
                rand2 = RANDOM.nextInt(Area.ROOMS.size()); // FIXME if rand 2 != rand 1
            }
            Room room1 = Area.ROOMS.get(randomRoom);
            int numHallways1 = room1.hallways().size();
            int numFailedAttempts1 = room1.hallwayAttempt - numHallways1;
            if (numHallways1 < numHallways || numFailedAttempts1 < numFailedAttempts) {
                room = room1;
                randomRoom = rand2;
            }
            // FIXME if no room is created at its end, delete hallway

            // build hallway within 3 attempts
            Hallway hallway = (Hallway) severalAttempts(room, null, 3);
            if (hallway.isInstanceCreated()) {

                // 33% chance of building hallway turn (almost 100% rate of successful construction)
                Hallway turn = buildTurn(hallway, 0.5);
                if (!turn.isTurn() || turn.length() > 2) {

                    // building new room at the end of hallway
                    // 100% of building new room (lower rate of successful construction, though)
                    Room r = buildRoom(turn);
                }
            }
            System.out.println("room " + randomRoom + " has " + room.hallways().size() + " " +
                    "hallways\n");
        }
    }

    private Area severalAttempts(Room room, Hallway hallway, int numAttempts) {
        Area area = room == null ? new Room(WORLD, hallway, RANDOM, 0) :
                                   new Hallway(WORLD, room, RANDOM);

        int attempt = 1;
        while (attempt < numAttempts && !area.isInstanceCreated()) {
            area = room == null ? new Room(WORLD, hallway, RANDOM, attempt) :
                                  new Hallway(WORLD, room, RANDOM);
            attempt++;
        }

        totalNumOfTiles += area.isInstanceCreated() ? area.numOfTiles() : 0;
        return area;
    }

    /**
     * 33% chance of choosing to build a hallway turn (in one attempt, since construction success
     * rate is very high). If a valid hallway turn is built, returns it. Otherwise, returns the
     * original hallway.
     * // FIXME number of turns
     */
    private Hallway buildTurn(Hallway hallway, double probability) {
        if (hallway.length() < 3) {
            return hallway;
        }
        boolean buildTurn = RandomUtils.bernoulli(RANDOM, probability);
        if (buildTurn) {
            hallway = buildSeveralTurns(hallway);
        }
        return hallway;
    }

    private Hallway buildSeveralTurns(Hallway hallway) {
        int numOfTurns = RANDOM.nextInt(2) + 1;
        Hallway turn = hallway;
        for (int i = 0; i < numOfTurns; i++) {
            turn = buildOneTurn(turn);
        }
        return turn;
    }

    private Hallway buildOneTurn(Hallway hallway) {
        Hallway turn = Hallway.hallwayTurn(WORLD, hallway);
        if (turn.isInstanceCreated()) {
            totalNumOfTiles += turn.numOfTiles() - 1;
            return turn;
        }
        return hallway;
    }

    /**
     * 100% chance of choosing to build a new room (within 6 attempts, since construction success
     * rate can be low).
     */
    private Room buildRoom(Hallway hallway) {
        return (Room) severalAttempts(null, hallway, 6);
    }

    private double load() {
        return (float) totalNumOfTiles / (WIDTH * HEIGHT);
    }

    public static void main(String[] args) {
        Stopwatch sw = new Stopwatch();

        TERenderer teRenderer = new TERenderer();
        teRenderer.initialize(80, 30);

        long seed = 999_999_999;
        Random r = new Random(seed);
        WorldGenerator wg = new WorldGenerator(80, 30, r);

        teRenderer.renderFrame(wg.WORLD);
        System.out.println("\nTotal time: " + sw.elapsedTime());
    }

}
