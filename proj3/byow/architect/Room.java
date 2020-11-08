package byow.BuildingBlock;

import byow.BuildingBlock.Space.*;
import byow.TileEngine.TETile;

import java.util.*;

/**
 * This class creates a Room of random size and random position at the end of a hallway
 * {@link Hallway}.
 *
 * If a valid Room instance is created, adds the source hallway object to its list of adjacent
 * hallways and initializes its number of hallway construction attempts as 0. Criteria of validity:
 *      - 3 <= length <= 8
 *      - no overlap with existing rooms and hallways.
 *
 * Stays one space away from borders for walls, and then for aesthetics, stays 3 spaces away from
 * left and right borders and 4 spaces from upper and bottom borders.
 */
public class Room implements Area {

    private static final int MAX_LENGTH = 8;            // room class's size maximum limit
    private static final int MIN_LENGTH = 3;            // room class's size minimum limit

    private final int WIDTH, HEIGHT;                    // width and height of the world
    private final Random RANDOM;                        // random object used for all construction
    private int width, height;                          // this room's width and height
    private Position position;                          // this room's upper-left corner position
    private Map<Direction, List<Position>> borders;     // all possible positions to start a hallway
    private List<Hallway> hallways;                     // all hallways adjacent to this room
    protected int hallwayAttempt;                       // number of attempts to build hallway

    private boolean roomCreated = false;


    /**
     * Room constructor. If a valid Room instance is created, adds the source hallway object to its
     * list of adjacent hallways and initializes its number of hallway construction attempts.
     *
     * @param world  the world onto which a valid room object will be added
     * @param hallway  the hallway object at the end of which this room is to be constructed
     * @param random  the random object used for all construction
     */
    public Room(TETile[][] world, Hallway hallway, Random random) {
        WIDTH = world.length;
        HEIGHT = world[0].length;
        this.RANDOM = random;

        createRandomRoom(hallway);

        if (AREAS.isEmpty() || isValid(List.of(hallway))) {
            roomCreated = true;
            hallways = new LinkedList<>();
            hallways.add(hallway);  // null case of hallway is dealt with elsewhere
            hallwayAttempt = 0;
            generateBorders();

            ROOMS.add(this);
            AREAS.add(this);
            drawToWorld(world);
        }
    }

    /**
     * Initializes random room width and height. Then, since starter-room doesn't come from any
     * hallway, its position is also randomly generated. For subsequent rooms, room's parameters
     * are refined and redetermined if necessary, so as to make it connect with given hallway.
     */
    private void createRandomRoom(Hallway hallway) {
        width = RANDOM.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;
        height = RANDOM.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;

        if (hallway == null) {
            int roomX = randomInBoundsX();
            int roomY = randomInBoundsY();
            position = new Position(roomX, roomY);
        } else {
            connectHallway(hallway);
        }
    }

    /**
     * Randomly determines room position while making sure it connects with hallway. Refines
     * room's width and height accordingly so that it doesn't touch world borders.
     */
    private void connectHallway(Hallway hallway) {
        int xEndpoint = hallway.endPosition().getX();
        int yEndpoint = hallway.endPosition().getY();

        int roomX, roomY;

        if (hallway.orientation() == Orientation.HORIZONTAL) {
            if (hallway.direction() == Direction.EAST) {
                roomX = xEndpoint + 1;
                int maxWidth = WIDTH - roomX - 4;
                width = randomLengthWithin(maxWidth);
            } else {
                int maxWidth = xEndpoint - 4;
                width = randomLengthWithin(maxWidth);
                roomX = xEndpoint - width;
            }
            roomY = Math.max(height + 4, Math.min(HEIGHT - 6, RANDOM.nextInt(height) + yEndpoint));
        } else {
            if (hallway.direction() == Direction.NORTH) {
                int maxHeight = HEIGHT - yEndpoint - 6;
                height = randomLengthWithin(maxHeight);
                roomY = yEndpoint + height;
            } else {
                roomY = yEndpoint - 1;
                int maxHeight = roomY - 4;
                height = randomLengthWithin(maxHeight);
            }
            roomX = Math.max(4, Math.min(WIDTH - width - 4,
                    RANDOM.nextInt(width) + (xEndpoint - width + 1)));
        }

        position = new Position(roomX, roomY);
    }

    /**
     * Given the room's width, height, and position, collects all positions on its four sides for
     * hallway construction - these positions are possible starting points for hallways.
     */
    private void generateBorders() {
        borders = new HashMap<>();
        List<Position> north = new LinkedList<>();
        List<Position> south = new LinkedList<>();
        List<Position> east = new LinkedList<>();
        List<Position> west = new LinkedList<>();

        int roomX = position.getX();
        int roomY = position.getY();

        for (int x = 0; x < width; x++) {
            int currX = roomX + x;
            north.add(new Position(currX, roomY + 1));
            south.add(new Position(currX, roomY - height));
        }
        for (int y = 0; y < height; y++) {
            int currY = roomY - y;
            east.add(new Position(roomX + width, currY));
            west.add(new Position(roomX - 1, currY));
        }

        borders.put(Direction.NORTH, north);
        borders.put(Direction.SOUTH, south);
        borders.put(Direction.EAST, east);
        borders.put(Direction.WEST, west);
    }

    /**
     * Randomly returns a valid x-coordinate that doesn't touch the borders of the world.
     */
    private int randomInBoundsX() {
        return 4 + RANDOM.nextInt(WIDTH - width - 8);
    }

    /**
     * Randomly returns a valid y-coordinate that doesn't touch the borders of the world.
     */
    private int randomInBoundsY() {
        return 4 + height + RANDOM.nextInt(HEIGHT - height - 8);
    }

    /**
     * If the maximum possible length is greater than MIN_LENGTH, returns a random integer in range
     * [MIN_LENGTH, Math.min(max, MAX_LENGTH)]. Otherwise, returns -1. This is to guarantee that if
     * maxLength is viable, Random doesn't return an unusable length lesser than MIN_LENGTH.
     */
    private int randomLengthWithin(int max) {
        int len = -1;
        if (max >= MIN_LENGTH) {
            len = RANDOM.nextInt(Math.min(max, MAX_LENGTH) - MIN_LENGTH + 1) + MIN_LENGTH;
        }
        return len;
    }

    @Override
    public boolean isInstanceCreated() {
        return roomCreated;
    }

    @Override
    public Position position() {
        return position;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    public Map<Direction, List<Position>> borders() {
        return borders;
    }

    public List<Hallway> hallways() {
        return hallways;
    }

    @Override
    public int numOfTiles() {
        return width * height;
    }

    @Override
    public Random random() {
        return RANDOM;
    }

    @Override
    public int attempt() {
        return 0;
    }

}
