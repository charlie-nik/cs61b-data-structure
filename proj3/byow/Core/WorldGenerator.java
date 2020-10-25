package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class WorldGenerator {
    private final int WIDTH;
    private final int HEIGHT;
    public final TETile[][] WORLD;
    private int numOccupied;
    private final Random RANDOM;


    public WorldGenerator(int width, int height, Random r) {
        WIDTH = width;
        HEIGHT = height;
        WORLD = new TETile[width][height];
        numOccupied = 0;
        RANDOM = r;

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
        Room starterRoom = new Room(WORLD, null, RANDOM);
        numOccupied += starterRoom.numOfTiles();

        while (load() < 0.35) {
            int randomRoom = RANDOM.nextInt(Room.ROOMS.size());
            Room room = Room.ROOMS.get(randomRoom);

            // FIXME if fails, it'll always fail cuz it's the same seed......
            Hallway hallway = new Hallway(WORLD, room, RANDOM);
            if (hallway.isHallwayCreated()) { // FIXME too much wasted work
                numOccupied += hallway.numOfTiles();

                Hallway turn = Hallway.hallwayTurn(WORLD, hallway); // FIXME too likely
                if (turn.isHallwayCreated()) {
                    numOccupied += turn.numOfTiles();
                    hallway = turn;
                }

                Room newRoom = new Room(WORLD, hallway, RANDOM);  // FIXME: not every time is a new room to be created
                numOccupied += newRoom.numOfTiles();
            }
        }
    }

    private double load() {
        return (float) numOccupied / (WIDTH * HEIGHT);
    }

    public static void main(String[] args) {
        TERenderer teRenderer = new TERenderer();
        teRenderer.initialize(80, 30);

        Random r = new Random();
        WorldGenerator wg = new WorldGenerator(80, 30, r);

        teRenderer.renderFrame(wg.WORLD);
    }

}
