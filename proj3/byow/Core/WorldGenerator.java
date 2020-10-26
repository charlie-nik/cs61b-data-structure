package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Random;

public class WorldGenerator {
    private final int WIDTH;
    private final int HEIGHT;
    private int totalNumOfTiles;
    private final Random RANDOM;

    public final TETile[][] WORLD;


    public WorldGenerator(int width, int height, Random random) {
        WIDTH = width;
        HEIGHT = height;
        WORLD = new TETile[width][height];
        totalNumOfTiles = 0;
        RANDOM = random;

        // fillBackground();
        // generateWorld();
    }

    private void fillBackground() {
        for (int i = 0; i < WORLD.length; i++) {
            for (int j = 0; j < WORLD[0].length; j++) {
                WORLD[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void generateWorld() {

        // build first room within 3 attempts
        threeAttempts(null, null);

        while (load() < 0.3) {
            int randomRoom = RANDOM.nextInt(Room.ROOMS.size());
            Room room = Room.ROOMS.get(randomRoom);

            // build hallway within 3 attempts
            Hallway hallway = (Hallway) threeAttempts(room, null);

            if (hallway.isInstanceCreated()) {
                // 1/3 chance of hallway turn
                Hallway turn = buildTurn(hallway);

                if (!turn.isTurn() || turn.length() > 2) {
                    // 2/3 chance of building new room at the end of hallway
                    // if hallway turns and new branch < 3, new room will overlap with old room
                    buildRoom(turn);
                }
            }
        }
    }

    private Area threeAttempts(Room room, Hallway hallway) {
        Area area = room == null ? new Room(WORLD, hallway, RANDOM, 0) :
                                   new Hallway(WORLD, room, RANDOM);

        int attempt = 1;
        while (attempt < 3 && !area.isInstanceCreated()) {
            area = room == null ? new Room(WORLD, hallway, RANDOM, attempt) :
                                  new Hallway(WORLD, room, RANDOM);
            attempt++;
        }

        totalNumOfTiles += area.isInstanceCreated() ? area.numOfTiles() : 0;
        return area;
    }

    /**
     * 1/3 chance of choosing to build a hallway turn (in one attempt). If a valid hallway turn
     * is built, returns it. Otherwise, returns the original hallway.
     */
    private Hallway buildTurn(Hallway hallway) {
        System.out.println("turn creation:");
        if (hallway.length() < 3) {
            return hallway;
        }
        int randomInt = RANDOM.nextInt(3);
        if (randomInt == 2) {
            System.out.println("    tried");
            Hallway turn = Hallway.hallwayTurn(WORLD, hallway);
            if (turn.isInstanceCreated()) {
                System.out.println("    succeeded");
                totalNumOfTiles += turn.numOfTiles() - 1;
                return turn;
            }
        }
        return hallway;
    }

    /**
     * 100% chance of choosing to build a new room (within 3 attempts).
     */
    private Room buildRoom(Hallway hallway) {
        System.out.println("room creation:");
        int randomInt = RANDOM.nextInt(5);
        if (true) {
            System.out.println("    tried");
            return (Room) threeAttempts(null, hallway);
        }
        return null;
    }

    private void refineRoomSelection(Room room) {
        if (Area.ROOMS.size() > 5) {
            if (room.hallways().size() > 2 || (room.hallwayAttempt - room.hallways().size() > 4)) {
                Area.ROOMS.remove(room);
            }
        }
    }

    private double load() {
        return (float) totalNumOfTiles / (WIDTH * HEIGHT);
    }

    public static void main(String[] args) {
        Stopwatch sw = new Stopwatch();

        TERenderer teRenderer = new TERenderer();
        teRenderer.initialize(80, 30);

        int seed = 333_333_33;
        Random r = new Random(seed);
        WorldGenerator wg = new WorldGenerator(80, 30, r);
        wg.fillBackground();

        wg.threeAttempts(null, null);

        int N = 60;
        for (int i = 0; i < N; i++) {
            System.out.println("no." + i);
            int randomRoom = wg.RANDOM.nextInt(Room.ROOMS.size());
            Room room = Room.ROOMS.get(randomRoom);

            // build hallway within 3 attempts
            System.out.println("hallway creation:\n    tried from room " + randomRoom);
            Hallway hallway = (Hallway) wg.threeAttempts(room, null);

            if (hallway.isInstanceCreated()) {
                System.out.println("    succeeded");
                // 1/3 chance of hallway turn
                Hallway turn = wg.buildTurn(hallway);

                if (!turn.isTurn() || turn.length() > 2) {
                    // 2/3 chance of building new room at the end of hallway
                    // if hallway turns and new branch < 3, new room will overlap with old room
                    Room r1 = wg.buildRoom(turn);
                    if (r1 != null && r1.isInstanceCreated()) {
                        System.out.println("    succeeded");
                    }
                }
            }

            wg.refineRoomSelection(room);
            System.out.println("");
            System.out.println("room " + randomRoom + " hallways: " + room.hallways().size() + "\n\n");
        }


        teRenderer.renderFrame(wg.WORLD);
        System.out.println(wg.load());
        System.out.println("\nTotal time: " + sw.elapsedTime());
    }

}
