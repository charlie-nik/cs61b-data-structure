package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.SpaceUtils.*;

import java.util.Random;

/**
 * A class for rooms and hallways.
 */
public class Areaaa {
    private static final Random RANDOM = new Random();
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    // TODO: add a disjoint set to keep track of dynamic connectivity？

    /**
     * Helper class to designate an area.
     */
    private static class Occupant {
        public String type;
        public int width, height;
        public Position pos;
        Occupant(String type, int width, int height, Position pos) {
            this.type = type;
            this.width = width;
            this.height = height;
            this.pos = pos;
        }
        public void resetPos(Position pos) {
            this.pos = pos;
        }
    }

    /**
     * Creates a single room with random value of WIDTH and HEIGHT at a random position
     * that doesn't overlap with existing rooms/hallways.
     * If there is no available space for the room, returns null.
     */
    private static Occupant createRoom(TETile[][] world) {
        // room width and height must be larger than 1, for otherwise it's a hallway
        int width = RANDOM.nextInt(6) + 2;
        int height = RANDOM.nextInt(6) + 2;

        Occupant room = new Occupant("room", width, height, randomInBoundsPos(width, height));

        int totalPossibilities = (WIDTH - width - 1) * (HEIGHT - height - 1);
        for (int i = 0; i < totalPossibilities; i++) {
            if (checkVacancy(world, room)) {
                return room;
            }
            room.resetPos(randomInBoundsPos(width, height));
        }
        return null;
    }

    /**
     * Adds a room to the world.
     */
    public static void addRoom(TETile[][] world) {
        Occupant room = createRoom(world);
        if (room != null) {
            addArea(world, room);
        }
    }

    /**
     * Creates a hallway with a random length, either horizontal or vertical，
     * that doesn't overlap with existing rooms/hallways.
     */
    private static Occupant createHallway(TETile[][] world) {
        int direction = RANDOM.nextInt(2);
        return createHallway(world, direction);
    }

    /**
     * Creates a hallway with the given direction (to facilitate hallway turns).
     */
    private static Occupant createHallway(TETile[][] world, int direction) {
        int width = 1;
        int height = 1;

        if (direction == 0) {
            width = 1 + RANDOM.nextInt(15);  // horizontal
        } else if (direction == 1) {
            height = 1 + RANDOM.nextInt(15); // vertical
        }

        Occupant hallway = new Occupant("hallway", width, height, randomInBoundsPos(width, height));

        int totalPossibilities = (WIDTH - width - 1) * (HEIGHT - height - 1);
        for (int i = 0; i < totalPossibilities; i++) {
            if (checkVacancy(world, hallway)) {
                return hallway;
            }
            hallway.resetPos(randomInBoundsPos(width, height));
        }
        return null;
    }

    /**
     *
     */
    private static Occupant hallwayTurn(Occupant prevHallway) {
        int width, height, newX, newY;

        if (prevHallway.width > prevHallway.height) {
            // horizontal
            int prevXStart = prevHallway.pos.getX();
            int prevXEnd = prevXStart + prevHallway.width;
            int prevY = prevHallway.pos.getY();

            // to ensure the new hallway intersect with the old one
            newX = RANDOM.nextInt(prevHallway.width) + prevXStart;
            newY = RANDOM.nextInt(prevY) + 1;
            height = RANDOM.nextInt(HEIGHT - prevY - 1) + (prevY - newY);
            width = 1;

        } else {
            // vertical or square
            int prevYStart = prevHallway.pos.getY(); // FIXME: redundant
            int prevYEnd = prevYStart - prevHallway.height;
            int prevX = prevHallway.pos.getX();

            // again, to ensure the two hallways intersect with each other
            newX = RANDOM.nextInt(prevX) + 1;
            newY = RANDOM.nextInt(prevHallway.height) + prevYEnd;
            width = RANDOM.nextInt(WIDTH - prevX - 1) + (prevX - newX);
            height = 1;
        }

        Position pos = new Position(newX, newY);
        return new Occupant("hallway", width, height, pos);
    }

    /**
     * Adds a hallway to the world. Randomly determines whether to add a turn to it
     * or not (by creating a new hallway that intersects with it).
     */
    public static void addHallway(TETile[][] world) {
        Occupant hallway = createHallway(world);
        addArea(world, hallway);

        // int turn = RANDOM.nextInt(2);
        int turn = 1;
        if (turn == 0) {
            Occupant hallway2 = hallwayTurn(hallway);
            addArea(world, hallway2);
        }
    }

    /**
     * Adds the given area to the world.
     */
    private static void addArea(TETile[][] world, Occupant area) {
        TETile tile = TETile.colorVariant(Tileset.MOUNTAIN, 32, 32, 32, RANDOM);
        for (int x = 0; x < area.width; x++) {
            for (int y = 0; y < area.height; y++) {
                int currX = area.pos.getX() + x;
                int currY = area.pos.getY() - y;

                world[currX][currY] = tile;
            }
        }
    }

    /**
     * Generates a random position that doesn't touch the borders of the world
     * in order to leave space for walls.
     * Used for rooms and hallways.
     */
    private static Position randomInBoundsPos(int width, int height) {
        int x = 1 + RANDOM.nextInt(WIDTH - width - 2);
        int y = height + RANDOM.nextInt(HEIGHT - height - 2);
        return new Position(x, y);
    }

    /**
     * Returns true if there's an empty space greater than equal than the input area, false otherwise.
     */
    private static boolean checkVacancy(TETile[][] world, Occupant area) {
        for (int x = 0; x < area.width; x++) {
            for (int y = 0; y < area.height; y++) {
                int currX = area.pos.getX() + x;
                int currY = area.pos.getY() - y;
                if (!world[currX][currY].equals(Tileset.NOTHING)) {
                    return false;
                }
            }
        }
        return true;
    }
}
