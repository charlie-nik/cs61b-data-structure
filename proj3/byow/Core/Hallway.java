/********************************************************************************************
 * This class creates a hallway that starts from a random point in a room and goes in a
 * random direction for a random length.
 *
 * Criteria of validity:
 *
 *    - 2 <= length <= 8
 *    - no overlap with existing rooms and hallways
 *
 * If a valid hallway is created within 3 attempts, draws it to the world; if not, quit.
 *
 *******************************************************************************************/
package byow.Core;

import byow.Core.SpaceUtils.*;
import byow.TileEngine.TETile;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Hallway implements Area {
    private final Random RANDOM;
    private final Position startPosition;
    private final Direction direction;
    private final int length;
    private static final int MAX_LENGTH = 8;

    private boolean hallwayCreated = false;

    //region Constructors
    //----------------------------------------------------------------------------------------
    /**
     * Primary public constructor, accessible for world-generating engine.
     */
    public Hallway(TETile[][] world, Room room, Random r) {
        this(room, null, null, -1, r);

        if (isValid(room)) {
            hallwayCreated = true;
            AREAS.add(this);
            addToWorld(world);
        }
    }

    /**
     * Unless otherwise specified, a hallway instance is constructed with random direction,
     * random position, and random length.
     */
    private Hallway(Room room, Direction dir, Position pos, int len, Random r) {
        RANDOM = r;
        direction = dir == null ? randomDirection() : dir;
        startPosition = pos == null ? randomStartPos(room, direction) : pos;
        length = len < 0 ? randomLength(direction, startPosition) : len;
    }
    //endregion


    //region Random Parameter Generators
    //----------------------------------------------------------------------------------------
    /**
     * Randomly returns a direction: east, west, north, or south.
     */
    private Direction randomDirection() {
        int randomDirection = RANDOM.nextInt(4);

        if (randomDirection == 0) {
            return Direction.EAST;
        } else if (randomDirection == 1) {
            return Direction.WEST;
        } else if (randomDirection == 2) {
            return Direction.NORTH;
        } else {
            return Direction.SOUTH;
        }
    }

    /**
     * Given the direction in which the hallway will depart from the room,
     * randomly returns a start position along the respective side of the room.
     * For example,
     *
     *   - if direction is EAST, returns a position on the RIGHT of the room
     *   - if direction is WEST, returns a position on the LEFT of the room
     *   - if direction is NORTH, returns a position on the TOP of the room
     *   - if direction is SOUTH, returns a position on the BOTTOM of the room
     *
     */
    private Position randomStartPos(Room room, Direction dir) {
        List<Position> entrances = new LinkedList<>();

        int x = room.position().getX();
        int y = room.position().getY();

        if (SpaceUtils.orientation(dir) == Orientation.HORIZONTAL) {
            for (int i = 0; i < room.height(); i++) {
                if (dir == Direction.EAST) {
                    entrances.add(new Position(x + room.width(), y - i));
                } else {
                    entrances.add(new Position(x - 1, y - i));
                }
            }
        } else {
            for (int i = 0; i < room.width(); i++) {
                if (dir == Direction.NORTH) {
                    entrances.add(new Position(x + i, y + 1));
                } else {
                    entrances.add(new Position(x + i, y - room.height()));
                }
            }
        }

        return entrances.get(RANDOM.nextInt(entrances.size()));
    }

    /**
     * Given the direction and start position of a hallway, randomly returns a viable length value.
     */
    private int randomLength(Direction dir, Position startPos) {
        int len = -1;
        int maxLength;

        if (dir == Direction.EAST) {
            maxLength = WIDTH - startPos.getX() - 1;
        } else if (dir == Direction.WEST) {
            maxLength = startPos.getX();
        } else if (dir == Direction.NORTH) {
            maxLength = HEIGHT - startPos.getY() - 1;
        } else {
            maxLength = startPos.getY();
        }

        if (maxLength > 1) {
            len = RANDOM.nextInt(Math.min(MAX_LENGTH, maxLength) - 1) + 2;
        }

        return len;
    }
    //endregion


    //region Hallway Turn
    //----------------------------------------------------------------------------------------
    /**
     * Randomly constructs and returns a new hallway at the end of a given hallway
     * so as to create a L-shaped turn.
     */
    public static Hallway hallwayTurn(TETile[][] world, Hallway hallway) {
        Direction dir = randomTurnDirection(hallway);
        Position pos = hallway.endPosition();
        int len = hallway.randomLength(dir, pos);

        Hallway turn = new Hallway(null, dir, pos, len, hallway.RANDOM);

        if (turn.isValid(hallway)) {
            turn.hallwayCreated = true;
            turn.addToWorld(world);
            AREAS.add(turn);
        }

        return turn;
    }

    /**
     * Randomly returns a new direction to create an L-shaped turn with the given hallway.
     */
    private static Direction randomTurnDirection(Hallway hallway) {
        int randomTurn = hallway.RANDOM.nextInt(2);

        if (hallway.orientation() == Orientation.HORIZONTAL) {
            return randomTurn == 0 ? Direction.NORTH : Direction.SOUTH;
        } else {
            return randomTurn == 1 ? Direction.EAST : Direction.WEST;
        }
    }
    //endregion


    //region Accessors
    //----------------------------------------------------------------------------------------
    /**
     * Returns true if a valid hallway instance is created.
     */
    public boolean isHallwayCreated() {
        return hallwayCreated;
    }

    /**
     * Since this method is used for world drawing, it should return the upper-left tile
     * of the hallway. It shouldn't return the tile from which a hallway actually starts.
     */
    @Override
    public Position position() {
        return direction == Direction.EAST || direction == Direction.SOUTH ?
                startPosition : endPosition();
    }

    /**
     * This method, on the other hand, should return the tile where the hallway actually ends
     * since this information is used for room construction.
     */
    public Position endPosition() {
        int x = startPosition.getX();
        int y = startPosition.getY();

        if (direction == Direction.EAST) {
            return new Position(x + length - 1, y);
        } else if (direction == Direction.WEST) {
            return new Position(x - length + 1, y);
        } else if (direction == Direction.NORTH) {
            return new Position(x, y + length - 1);
        } else {
            return new Position(x, y - length + 1);
        }

    }

    public Direction direction() {
        return direction;
    }

    public Orientation orientation() {
        return SpaceUtils.orientation(direction);
    }

    public int length() {
        return length;
    }

    @Override
    public int width() {
        return direction == Direction.EAST || direction == Direction.WEST ? length : 1;
    }

    @Override
    public int height() {
        return direction == Direction.NORTH || direction == Direction.SOUTH ? length : 1;
    }

    @Override
    public int numOfTiles() {
        return length;
    }
    //endregion

}
