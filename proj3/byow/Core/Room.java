package byow.Core;

import byow.Core.SpaceUtils.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Constructs a room of random size at the end of a hallway, which was going in
 * direction DIRECTION before it stopped at position(x, y).
 * Room's width and height should be greater than 1, for otherwise it's a hallway,
 * and lesser than 8.
 * Room shouldn't overlap with existing rooms. FIXME: or hallways!!!
 * If a valid room is created within 5 attempts, it is added to the ROOMS list.
 */
public class Room implements Area {
    public static final List<Room> ROOMS = new LinkedList<>();
    private Position pos;
    private int width, height;

    public Room(int x, int y, Direction direction) {
        int numAttempts = RANDOM.nextInt(5);
        for (int i = 0; i < numAttempts; i++) {

            createRandomRoom(x, y, direction);

            if (!checkOverlap(pos) && (width > 0) && (height > 0)) {
                ROOMS.add(this);
                break;
            }
        }
    }

    private void createRandomRoom(int x, int y, Direction direction) {
        width = RANDOM.nextInt(6) + 2;
        height = RANDOM.nextInt(6) + 2;
        int roomX = randomInBoundsX(width);
        int roomY = randomInBoundsY(height);

        // Connects room with hallway.
        // Doesn't apply to starter-room which doesn't have any direction.
        int limitSize = 0;
        Directionality directionality = SpaceUtils.directionality(direction);

        if (directionality == Directionality.HORIZONTAL) {
            if (direction == Direction.EAST) {
                limitSize = WIDTH - x - 2;
                roomX = x + 1;
            } else if (direction == Direction.WEST) {
                limitSize = x - 1;
                roomX = x - width;
            }
            width = Math.min(7, RANDOM.nextInt(limitSize + 1));
            roomY = Math.min(HEIGHT - 1, (RANDOM.nextInt(height) + y));

        } else if (directionality == Directionality.VERTICAL) {
            if (direction == Direction.NORTH) {
                limitSize = HEIGHT - y - 2;
                roomY = y + height;
            } else if (direction == Direction.SOUTH) {
                limitSize = y - 1;
                roomY = y - 1;
            }
            height = Math.min(7, RANDOM.nextInt(limitSize + 1));
            roomX = Math.max(1, RANDOM.nextInt(width) + (x - width + 1));
        }

        pos = new Position(roomX, roomY);
    }

    /**
     * Returns true if room overlaps with existing ones, false otherwise.
     */
    private boolean checkOverlap(Position pos) {
        int xStart = pos.getX();
        int yStart = pos.getY();
        int xEnd = xStart + width - 1;
        int yEnd = yStart - height + 1;

        for (Room room : ROOMS) {
            int tempXStart = room.pos.getX();
            int tempYStart = room.pos.getY();
            int tempXEnd = tempXStart + room.width - 1;
            int tempYEnd = tempYStart - room.height + 1;

            if (xOverlap(xStart, xEnd, tempXStart, tempXEnd) &&
                    yOverlap(yStart, yEnd, tempYStart, tempYEnd)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if x-coordinates overlap.
     */
    private boolean xOverlap(int s1, int e1, int s2, int e2) {
        return ((s1 <= s2) && (e1 >= s2)) || ((s1 <= e2) && (e1 >= e2));
    }

    /**
     * Returns true if y-coordinates overlap.
     */
    private boolean yOverlap(int s1, int e1, int s2, int e2) {
        return ((s1 >= s2) && (e1 <= s2)) || ((s1 >= e2) && (e1 <= e2));
    }

    /**
     * Randomly returns a valid x-coordinate that doesn't touch the borders of the world.
     */
    private static int randomInBoundsX(int width) {
        return 1 + RANDOM.nextInt(WIDTH - width - 2);
    }

    /**
     * Randomly returns a valid y-coordinate that doesn't touch the borders of the world.
     */
    private static int randomInBoundsY(int height) {
        return height + RANDOM.nextInt(HEIGHT - height - 2);
    }

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
}
