/********************************************************************************************
 * Constructs a room of random size and position at the end of a hallway.
 *
 * Criteria of validity:
 *
 *    - 3 <= width/height <= 7
 *    - no overlap with existing rooms and hallways
 *
 * If a valid room is created within 3 attempts, draws it to the world. If not, quit.
 * FIXME: if fails, it gotta repeat a lot of stuff to make another try. RUNTIME OPTIMIZATION
 *
 *******************************************************************************************/
package byow.Core.BuildingBlock;

import byow.Core.SpaceUtils.*;
import byow.TileEngine.TETile;

import java.util.*;


public class Room implements Area {
    private final int WIDTH, HEIGHT;                      // width and height of WORLD
    private int width, height;                            // room instance's width and height
    private Position position;                            // room instance's upper-left position
    private Map<Direction, List<Position>> borders;       // possible positions to start a hallway
    private List<Hallway> hallways;                       // all hallways from this room instance
    public int hallwayAttempt;                            // number of attempts to build hallway
                                                               // from this room instance
    private static final int MAX_LENGTH = 10;              // room class's size maximum limit
    private static final int MIN_LENGTH = 3;              // room class's size minimum limit

    private final Random RANDOM;
    private final int ATTEMPT;
    private boolean roomCreated = false;


    public Room(TETile[][] world, Hallway hallway, Random random, int attempt) {
        WIDTH = world.length;
        HEIGHT = world[0].length;
        RANDOM = random;
        ATTEMPT = attempt;

        createRandomRoom(hallway);

        if (AREAS.isEmpty() || isValid(List.of(hallway))) {
            roomCreated = true;
            hallways = new LinkedList<>();
            hallways.add(hallway);  // hallway may be null
            hallwayAttempt = 0;
            generateBorders();

            ROOMS.add(this);
            AREAS.add(this);
            addToWorld(world);
        }
    }

    /**
     * Initializes random width and height.
     * Then, since starter-room doesn't come from any hallway, its position is also randomly
     * generated. For subsequent rooms, room's parameters are refined and redetermined if
     * necessary so as to make sure it connects with given hallway.
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
     * Randomly determines room position while making sure it connects with hallway.
     */
    private void connectHallway(Hallway hallway) {
        int xEndpoint = hallway.endPosition().getX();
        int yEndpoint = hallway.endPosition().getY();

        int roomX, roomY;

        if (hallway.orientation() == Orientation.HORIZONTAL) {
            if (hallway.direction() == Direction.EAST) {
                roomX = xEndpoint + 1;
                int maxWidth = WIDTH - roomX - 1;
                width = randomLengthWithin(maxWidth);
            } else {
                int maxWidth = xEndpoint - 1;
                width = randomLengthWithin(maxWidth);
                roomX = xEndpoint - width;
            }
            roomY = Math.max(height, Math.min(HEIGHT - 2, RANDOM.nextInt(height) + yEndpoint));
        } else {
            if (hallway.direction() == Direction.NORTH) {
                int maxHeight = HEIGHT - yEndpoint - 2;
                height = randomLengthWithin(maxHeight);
                roomY = yEndpoint + height;
            } else {
                roomY = yEndpoint - 1;
                int maxHeight = roomY;
                height = randomLengthWithin(maxHeight);
            }
            roomX = Math.max(1, Math.min(WIDTH - width - 1,
                    RANDOM.nextInt(width) + (xEndpoint - width + 1)));
        }

        position = new Position(roomX, roomY);
    }

    /*
    private List<int[]> validCorners(Hallway hallway) {
        List<int[]> corners = new LinkedList<>();

        int xEndpoint = hallway.endPosition().getX();
        int yEndpoint = hallway.endPosition().getY();
        int roomX, roomY;

        if (hallway.orientation() == Orientation.HORIZONTAL) {
            if (hallway.direction() == Direction.EAST) {
                roomX = xEndpoint + 1;
            } else {
                roomX = xEndpoint - 1;
            }
            for (int i = -height + 1; i < height; i++) {
                int currY = yEndpoint + i;
                if (isValidRoomCorner(roomX, currY, hallway)) {
                    corners.add(new int[]{roomX, currY});
                }
            }
         } else {
            if (hallway.direction() == Direction.NORTH) {
                roomY = yEndpoint + 1;
            } else {
                roomY = yEndpoint - 1;
            }
            for (int i = -width + 1; i < width; i++) {
                int currX = xEndpoint + i;
                if (isValidRoomCorner(currX, roomY, hallway)) {
                    corners.add(new int[]{currX, roomY});
                }
            }
        }

        return corners;
    }

    private boolean isValidRoomCorner(int x, int y, Hallway hallway) {
        if (x < 1 || x > WIDTH - 3 || y < 2 || y > HEIGHT - 2) {
            return false;
        }

        List<Integer> xKin = new LinkedList<>();
        List<Integer> yKin = new LinkedList<>();
        int xEndpoint = hallway.endPosition().getX();
        int yEndpoint = hallway.endPosition().getY();

        if (hallway.orientation() == Orientation.HORIZONTAL) {
            if (hallway.direction() == Direction.EAST) {
                xKin.add(xEndpoint + 1);
            } else {
                xKin.add(xEndpoint - 1);
            }
            xKin.add(xEndpoint);
            yKin.addAll(List.of(yEndpoint + 1, yEndpoint, yEndpoint - 1));
        } else {
            if (hallway.direction() == Direction.NORTH) {
                yKin.add(yEndpoint + 1);
            } else {
                yKin.add(yEndpoint - 1);
            }
            yKin.add(yEndpoint);
            xKin.addAll(List.of(xEndpoint - 1, xEndpoint, xEndpoint + 1));
        }

        return isValidPosition(x, y, xKin, yKin);
    }

    private boolean isValidPosition(int x, int y, List<Integer> xKin, List<Integer> yKin) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int currX = x + i;
                int currY = y + j;
                if (!xKin.contains(currX) && !yKin.contains(currY)) {
                    if (world[currX][currY] != Tileset.NOTHING) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

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
        borders.put(Direction.SOUTH,south);
        borders.put(Direction.EAST, east);
        borders.put(Direction.WEST, west);
    }

    //region Handy Tools
    //----------------------------------------------------------------------------------------
    /**
     * Randomly returns a valid x-coordinate that doesn't touch the borders of the world.
     */
    private int randomInBoundsX() {
        return 1 + RANDOM.nextInt(WIDTH - width - 2);
    }

    /**
     * Randomly returns a valid y-coordinate that doesn't touch the borders of the world.
     */
    private int randomInBoundsY() {
        return height + RANDOM.nextInt(HEIGHT - height - 2);
    }

    /**
     * If the maximum length value is greater than 1, returns a random integer in range
     * [2, Math.min(max, MAX_LENGTH)]. Otherwise, returns -1.
     */
    private int randomLengthWithin(int max) {
        int len = -1;
        if (max >= MIN_LENGTH) {
            len = RANDOM.nextInt(Math.min(max, MAX_LENGTH) - MIN_LENGTH + 1) + MIN_LENGTH;
        }
        return len;
    }
    //endregion


    //region Accessors
    //----------------------------------------------------------------------------------------
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
        return ATTEMPT;
    }
    //endregion

}
