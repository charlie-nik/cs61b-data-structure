package byow.BuildingBlock;

import byow.BuildingBlock.Space.*;
import byow.TileEngine.TETile;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class creates a Hallway that starts from a random point in a room and goes in a random
 * direction for a random length. If a valid Hallway instance is created, draws it to the world
 * and adds itself to the source Room's list of adjacent hallways; if not, quits trying. Either way,
 * increments the source Room's number of hallway construction attempts by one. Criteria of
 * validity: first, 2 <= length <= 10; second, no overlap with existing rooms and hallways. Stays
 * one space away from world borders for walls, and then three more spaces for aesthetics.
 */
public class Hallway implements Area {
    private final int WIDTH, HEIGHT;                    // width and height of WORLD
    private final Room room;                            // the room where hallway comes from
    private final Position startPosition, endPosition;  // hallway instance's start and end position
    private final Direction direction;                  // hallway instance's direction
    private final int length;                           // hallway instance's length
    private boolean isTurn = false;                     // whether hallway is a L-turn or not
    private static final int MIN_LENGTH = 3;            // hallway class's size minimum limit
    private static final int MAX_LENGTH = 10;           // hallway class's size maximum limit

    private final Random RANDOM;
    private final int attempt;
    private boolean hallwayCreated = false;

    /**
     * Primary public constructor, accessible to world-generating engine. Whenever the
     * constructor is called, increments the source Room's number of hallway construction attempts
     * by one whether the construction is successful or not. But if a valid Hallway instance is
     * created, adds itself to source Room's list of hallways, and removes its start position off
     * the Room's list of available points for hallway's starting position.
     */
    public Hallway(TETile[][] world, Room origin, Random random) {
        this(world, origin, null, null, -1, random);

        room.hallwayAttempt += 1;

        if (isValid(allKins())) {
            hallwayCreated = true;
            room.hallways().add(this);
            room.borders().get(direction).remove(startPosition);
            AREAS.add(this);
            drawToWorld(world);
        }
    }

    /**
     * All-purpose constructor.
     * Under general circumstances, a hallway instance is constructed with random direction, random
     * start position, and random length. In the case of hallway turns, direction and start
     * position are determined elsewhere in advance.
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
        endPosition = computeEndPosition();
    }

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
     *     - if direction is EAST, returns a position on the RIGHT of the room
     *     - if direction is WEST, returns a position on the LEFT of the room
     *     - if direction is NORTH, returns a position on the TOP of the room
     *     - if direction is SOUTH, returns a position on the BOTTOM of the room
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
            maxLength = WIDTH - startPos.getX() - 4;
        } else if (dir == Direction.WEST) {
            maxLength = startPos.getX() - 3;
        } else if (dir == Direction.NORTH) {
            maxLength = HEIGHT - startPos.getY() - 4;
        } else {
            maxLength = startPos.getY() - 3;
        }

        if (maxLength >= MIN_LENGTH) {
            len = enhancedRandom(Math.min(MAX_LENGTH, maxLength) - MIN_LENGTH + 1) + MIN_LENGTH;
        }
        return len;
    }

    /**
     * Given the direction, start position, and length of a hallway, computes its end position.
     */
    private Position computeEndPosition() {
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

    /**
     * Returns a list of areas with which the hallway is allowed to overlap. The list should
     * include hallway's source Room and all hallways that come out of that room in a direction
     * other than its own. Hallways leaving in the same direction are regarded as strangers, not a
     * kin.
     */
    private List<Area> allKins() {
        List<Area> kins = new LinkedList<>(List.of(room));
        for (Hallway hallway : room.hallways()) {
            if (hallway != null && hallway.orientation() != orientation()) {
                kins.add(hallway);
            }
        }
        return kins;
    }

    /**
     * Randomly constructs and returns a new hallway at the end of a given hallway so as to
     * create a L-shaped turn.
     */
    public static Hallway hallwayTurn(TETile[][] world, Hallway hallway) {
        Direction dir = randomTurnDirection(hallway);
        Position pos = hallway.endPosition();
        int len = hallway.randomLength(dir, pos);

        Hallway turn = new Hallway(world, null, dir, pos, len, hallway.RANDOM);

        if (turn.isValid(List.of(hallway))) {
            turn.hallwayCreated = true;
            turn.isTurn = true;
            AREAS.add(turn);
            turn.drawToWorld(world);
        }

        return turn;
    }

    /**
     * Randomly returns a new direction to create an L-shaped turn with the original hallway.
     */
    private static Direction randomTurnDirection(Hallway hallway) {
        int randomTurn = hallway.enhancedRandom(2);

        if (hallway.orientation() == Orientation.HORIZONTAL) {
            return randomTurn == 0 ? Direction.NORTH : Direction.SOUTH;
        } else {
            return randomTurn == 0 ? Direction.EAST : Direction.WEST;
        }
    }

    @Override
    public boolean isInstanceCreated() {
        return hallwayCreated;
    }

    /**
     * Since this method is used for world drawing, it returns the upper-left tile of the hallway.
     * It does NOT return the tile from which a hallway actually starts.
     */
    @Override
    public Position position() {
        return direction == Direction.EAST || direction == Direction.SOUTH ?
                startPosition : endPosition();
    }

    /**
     * This method, on the other hand, returns the tile where the hallway actually ends since
     * this information is used for subsequent room construction.
     */
    public Position endPosition() {
        return endPosition;
    }

    public Direction direction() {
        return direction;
    }

    public Orientation orientation() {
        if (direction == Direction.EAST || direction == Direction.WEST) {
            return Orientation.HORIZONTAL;
        } else {
            return Orientation.VERTICAL;
        }
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

}
