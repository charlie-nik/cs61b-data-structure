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
    private final int WIDTH, HEIGHT;                    // width and height of WORLD
    private final Room room;                            // the room where hallway comes from
    private final Position startPosition;               // hallway instance's start position
    private final Direction direction;                  // hallway instance's direction
    private final int length;                           // hallway instance's length
    private boolean isTurn = false;                     // whether hallway is a L-turn or not
    private static final int MAX_LENGTH = 8;            // hallway class's size limit

    private final Random RANDOM;
    private final int attempt;
    private boolean hallwayCreated = false;


    //region Constructors
    //----------------------------------------------------------------------------------------
    /**
     * Primary public constructor, accessible for world-generating engine.
     */
    public Hallway(TETile[][] world, Room origin, Random random) {
        this(world, origin, null, null, -1, random);

        room.hallwayAttempt += 1;

        if (isValid(allKins())) {
            hallwayCreated = true;
            room.hallways().add(this);
            AREAS.add(this);
            addToWorld(world);
        }
    }

    /**
     * All-purpose constructor.
     * For general hallways, a hallway instance is constructed with random direction, random
     * start position, and random length.
     * For hallway turns, direction and start position are determined elsewhere in advance.
     */
    private Hallway(TETile[][] world, Room origin, Direction dir, Position pos, int len, Random r) {
        WIDTH = world.length;
        HEIGHT = world[0].length;
        RANDOM = r;

        room = origin;
        attempt = origin != null ? room.hallwayAttempt : 0;     // hallway turn = 0 attempt
        direction = dir == null ? randomDirection() : dir;
        startPosition = pos == null ? randomStartPos(direction) : pos;
        length = len < 0 ? randomLength(direction, startPosition) : len;
    }
    //endregion


    //region Random Parameter Generators
    //----------------------------------------------------------------------------------------
    /**
     * Randomly returns a direction: east, west, north, or south.
     */
    private Direction randomDirection() {
        int randomDirection = enhancedRandom(4);

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
     * Given the direction in which the hallway will depart from the room randomly returns a
     * start position along the respective side of the room. For example,
     *
     *   - if direction is EAST, returns a position on the RIGHT of the room
     *   - if direction is WEST, returns a position on the LEFT of the room
     *   - if direction is NORTH, returns a position on the TOP of the room
     *   - if direction is SOUTH, returns a position on the BOTTOM of the room
     */
    private Position randomStartPos(Direction dir) {
        List<Position> roomBorder = room.borders().get(dir);
        return roomBorder.get(enhancedRandom(roomBorder.size()));
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

        len = maxLength > 1 ? enhancedRandom(Math.min(MAX_LENGTH, maxLength) - 1) + 2 : len;
        return len;
    }

    /**
     * Returns a list of areas with which the hallway is allowed to overlap. The list should
     * include hallway's origin room and all hallways that come out of that room in a direction
     * other than its own. Hallways leaving in the same direction are regarded and evaluated as
     * a regular stranger, not a kin.
     */
    private List<Area> allKins() {
        List<Area> kins = new LinkedList<>(List.of(room));
        for (Hallway hallway : room.hallways()) {
            if (hallway.orientation() != orientation()) {
                kins.add(hallway);
            }
        }
        return kins;
    }
    //endregion


    //region Hallway Turn
    //----------------------------------------------------------------------------------------
    /**
     * Randomly constructs and returns a new hallway at the end of a given hallway so as to
     * create a L-shaped turn.
     */
    public static Hallway hallwayTurn(TETile[][] world, Hallway hallway) {
        Direction dir = randomTurnDirection(hallway);
        Position pos = hallway.endPosition();
        int len = hallway.randomLength(dir, pos);

        Hallway turn = new Hallway(world, null, dir, pos, len, hallway.RANDOM);

        if (turn.isValid(List.of(hallway, hallway.room))) {
            turn.hallwayCreated = true;
            turn.isTurn = true;
            AREAS.add(turn);
            turn.addToWorld(world);
        }

        return turn;
    }

    /**
     * Randomly returns a new direction to create an L-shaped turn with the given hallway.
     */
    private static Direction randomTurnDirection(Hallway hallway) {
        int randomTurn = hallway.enhancedRandom(2);

        if (hallway.orientation() == Orientation.HORIZONTAL) {
            return randomTurn == 0 ? Direction.NORTH : Direction.SOUTH;
        } else {
            return randomTurn == 0 ? Direction.EAST : Direction.WEST;
        }
    }
    //endregion


    //region Accessors
    //----------------------------------------------------------------------------------------
    /**
     * Returns true if a valid hallway instance is created.
     */
    @Override
    public boolean isInstanceCreated() {
        return hallwayCreated;
    }

    /**
     * Since this method is used for world drawing, it should return the upper-left tile of the
     * hallway. It shouldn't return the tile from which a hallway actually starts.
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

    public boolean isTurn() {
        return isTurn;
    }

    @Override
    public int numOfTiles() {
        return length;
    }

    @Override
    public Random random() {
        return RANDOM;
    }

    @Override
    public int attempt() {
        return attempt;
    }
    //endregion

}
