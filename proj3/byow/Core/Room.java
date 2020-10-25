/********************************************************************************************
 * Constructs a room of random size and position at the end of a hallway.
 *
 * Criteria of validity:
 *
 *    - 2 <= width/height <= 5
 *    - no overlap with existing rooms and hallways
 *
 * If a valid room is created within 3 attempts, draws it to the world. If not, quit.
 * FIXME: if fails, it gotta repeat a lot of stuff to make another try. RUNTIME OPTIMIZATION
 * FIXME: runtime optimization idea: graph traversal?? extend to lesser occupied directions...
 * FIXME: lower the chance of superfluous failed attempts
 *
 *******************************************************************************************/
package byow.Core;

import byow.Core.SpaceUtils.*;
import byow.TileEngine.TETile;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Room implements Area {
    public static final List<Room> ROOMS = new LinkedList<>();
    private static final int MAX_LENGTH = 6;
    private final Random RANDOM;
    private int width, height;
    private Position pos;

    public Room(TETile[][] world, Hallway hallway, Random r) {
        RANDOM = r;
        int numAttempts = ROOMS.size() == 0 ? Integer.MAX_VALUE : 3;

        for (int i = 0; i < numAttempts; i++) {

            createRandomRoom(hallway);

            if (isValid(hallway)) {
                addToWorld(world);
                ROOMS.add(this);
                AREAS.add(this);
                break;
            }
        }
    }

    private void createRandomRoom(Hallway hallway) {
        // Initialize random size
        width = RANDOM.nextInt(MAX_LENGTH - 1) + 2;
        height = RANDOM.nextInt(MAX_LENGTH - 1) + 2;

        // Initialize random position
        // Except for starter-room which doesn't come from any hallway, room's position is
        // determined by making sure it is connected with the given hallway
        if (hallway == null) {
            int roomX = randomInBoundsX();
            int roomY = randomInBoundsY();
            pos = new Position(roomX, roomY);
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

        pos = new Position(roomX, roomY);
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
        if (max > 1) {
            len = RANDOM.nextInt(Math.min(max, MAX_LENGTH) - 1) + 2;
        }
        return len;
    }
    //endregion


    //region Accessors
    //----------------------------------------------------------------------------------------
    @Override
    public Position position() {
        return pos;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int numOfTiles() {
        return width * height;
    }
    //endregion

}
