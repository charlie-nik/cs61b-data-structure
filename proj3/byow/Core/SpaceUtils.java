package byow.Core;

public class SpaceUtils {

    public enum Direction {
        EAST, SOUTH, WEST, NORTH
    }
    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    public static Orientation orientation(Direction dir) {
        if (dir == Direction.EAST || dir == Direction.WEST) {
            return Orientation.HORIZONTAL;
        } else {
            return Orientation.VERTICAL;
        }
    }

    public static class Position {
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
    }

}
