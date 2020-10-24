package byow.Core;

import byow.Core.SpaceUtils.*;

/**
 * Hallway's length should be lesser than 16.
 */
public class Hallway implements Area {
    private Position pos;
    private Direction direction;
    private int length;

    public Hallway(Room room) {
        int numAttempts = RANDOM.nextInt(3);
        for (int i = 0; i < numAttempts; i++) {

            createRandomHallway(room);

            if (length > 0) {
                break;
            }
        }

        // FIXME: hallway turns
    }

    private void createRandomHallway(Room room) {
        int xStart = room.position().getX();
        int yStart = room.position().getY();
        int xEnd = xStart + room.width() - 1;
        int yEnd = yStart - room.height() + 1;

        int x, y, limit;
        int randomDirection = RANDOM.nextInt(4);

        if (randomDirection == 0) {
            direction = Direction.EAST;
            x = Math.min(xEnd + 1, WIDTH - 2);
            y = RANDOM.nextInt(room.height()) + yEnd;
            limit = WIDTH - x;
        } else if (randomDirection == 1) {
            direction = Direction.WEST;
            x = Math.max(xStart - 1, 1);
            y = RANDOM.nextInt(room.height()) + yEnd; // FIXME: repetitive
            limit = x;
        } else if (randomDirection == 2) {
            direction = Direction.NORTH;
            x = RANDOM.nextInt(room.width()) + xStart;
            y = Math.min(yStart + 1, HEIGHT - 2);
            limit = HEIGHT - y;
        } else {
            direction = Direction.SOUTH;
            x = RANDOM.nextInt(room.width()) + xStart;
            y = Math.max(yEnd - 1, 1);
            limit = y;
        }

        length = Math.min(15, RANDOM.nextInt(limit));
        pos = new Position(x, y);
    }

    private Directionality directionality() {
        return SpaceUtils.directionality(direction);
    }

    @Override
    public Position position() {
        return pos;
    }

    public Direction direction() {
        return direction;
    }

    @Override
    public int width() {
        return directionality() == Directionality.HORIZONTAL ? length : 1;
    }

    @Override
    public int height() {
        return directionality() == Directionality.VERTICAL ? length : 1;
    }

    @Override
    public int numOfTiles() {
        return length;
    }
}
