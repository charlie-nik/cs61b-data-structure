package byow.BuildingBlock;

import java.io.Serializable;
import java.util.Objects;

public class SpaceUtils {

    public enum Direction {
        EAST, SOUTH, WEST, NORTH
    }
    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    public static class Position implements Serializable {
        private int x;
        private int y;
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
        public void setX(int newX) {
            x = newX;
        }
        public void setY(int newY) {
            y = newY;
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

}
