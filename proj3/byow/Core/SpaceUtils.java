package byow.Core;

public class SpaceUtils {

    public enum Direction {
        EAST, SOUTH, WEST, NORTH
    }
    public enum Directionality {
        HORIZONTAL, VERTICAL
    }

    public static Directionality directionality(Direction dir) {
        if (dir == Direction.EAST || dir == Direction.WEST) {
            return Directionality.HORIZONTAL;
        } else if (dir == Direction.NORTH || dir == Direction.SOUTH) {
            return Directionality.VERTICAL;
        } else {
            return null; // when direction is null (ie. starter-room)
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
