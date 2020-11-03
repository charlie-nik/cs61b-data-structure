package byow.BuildingBlock;

import java.util.Objects;

/**
 * Utility class to designate a position. Public instance methods include: returning the instance's
 * x-coordinate or y-coordinate, and returning the position to the east, west, north, or west of
 * the instance. If two positions have the same x-coordinate and y-coordinate, they are ruled as
 * equal.
 */
public class Position {
    private final int x;
    private final int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public Position east() {
        return new Position(x + 1, y);
    }
    public Position west() {
        return new Position(x - 1, y);
    }
    public Position north() {
        return new Position(x, y + 1);
    }
    public Position south() {
        return new Position(x, y - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
