package bearmaps.hw4.rubikscube;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An implementation of the Rubik's cube.
 * Guided by Hug's implementation of the Board class.
 * The order of the six sides of cube is as follows:
 * 1-front, 2-back, 3-left, 4-right, 5-up, 6-down.
 */
public class Cube {

    private enum Color {
        RED, ORANGE, YELLOW, GREEN, BLUE, WHITE
    }

    private static class Face {
        private final Color[] wholeFace, top, right, down, left;
        private final Color center;
        public Face(Color[] face) {
            wholeFace = face;
            center = face[4];
            top = new Color[]{face[0], face[1], face[2]};
            right = new Color[]{face[2], face[5], face[8]};
            down = new Color[]{face[8], face[7], face[6]};
            left = new Color[]{face[6], face[3], face[0]};
        }
        public Face(Color center, Color[] top, Color[] right, Color[] down, Color[] left) {
            this.center = center;
            this.top = top;
            this.right = right;
            this.down = down;
            this.left = left;
            wholeFace = new Color[]{top[0], top[1], top[2], left[1], center,
                    right[1], down[2], down[1], down[0]};
        }
    }

    private final Face[] faces;
    private final Face frontFace, backFace, leftFace, rightFace, upFace, bottomFace;

    public Cube(Face[] faces) {
        this.faces = faces;
        frontFace = faces[0];
        backFace = faces[1];
        leftFace = faces[2];
        rightFace = faces[3];
        upFace = faces[4];
        bottomFace = faces[5];
    }

    public static Cube readCube(String filename) {
        In in = new In(filename);
        Face[] tiles = new Cube.Face[6];

        for (int i = 0; i < 6; i++) {
            String line = in.readLine();
            String[] tokens = line.trim().split("\\s+");
            assert tokens.length == 9;

            Color[] nineTiles = new Color[9];
            for (int j = 0; j < 9; j++) {
                String color = tokens[j];
                assert color.equals("red") || color.equals("orange") || color.equals("yellow") ||
                        color.equals("green") || color.equals("blue") || color.equals("white");
                switch (color) {
                    case "red" ->  nineTiles[j] = Color.RED;
                    case "orange" -> nineTiles[j] = Color.ORANGE;
                    case "yellow" -> nineTiles[j] = Color.YELLOW;
                    case "green" -> nineTiles[j] = Color.GREEN;
                    case "blue" -> nineTiles[j] = Color.BLUE;
                    case "white" -> nineTiles[j] = Color.WHITE;
                }
            }

            Face face = new Face(nineTiles);
            switch (face.center) {
                case RED -> tiles[0] = face;
                case ORANGE -> tiles[1] = face;
                case YELLOW -> tiles[2] = face;
                case GREEN -> tiles[3] = face;
                case BLUE -> tiles[4] = face;
                case WHITE -> tiles[5] = face;
            }
        }
        return new Cube(tiles);
    }

    public List<Cube> neighbors() {
        List<Cube> neighbors = new ArrayList<>();

        neighbors.add(turnCube("frontFace", "clockwise"));
        neighbors.add(turnCube("frontFace", "counterClockwise"));

        neighbors.add(turnCube("backFace", "clockwise"));
        neighbors.add(turnCube("backFace", "counterClockwise"));

        neighbors.add(turnCube("leftFace", "clockwise"));
        neighbors.add(turnCube("leftFace", "counterClockwise"));

        neighbors.add(turnCube("rightFace", "clockwise"));
        neighbors.add(turnCube("rightFace", "counterClockwise"));

        neighbors.add(turnCube("upFace", "clockwise"));
        neighbors.add(turnCube("upFace", "counterClockwise"));

        neighbors.add(turnCube("bottomFace", "clockwise"));
        neighbors.add(turnCube("bottomFace", "counterClockwise"));

        return neighbors;
    }

    // position the target face as the front face
    private Cube turnCube(String targetFace, String direction) {
        assert targetFace.equals("frontFace") || targetFace.equals("backFace") || targetFace.equals("leftFace") ||
                targetFace.equals("rightFace") || targetFace.equals("upFace") || targetFace.equals("bottomFace");
        // targetFace = "frontFace"
        Face front = frontFace;
        Face back = backFace;
        Face left = leftFace;
        Face right = rightFace;
        Face up = upFace;
        Face bottom = bottomFace;
        switch (targetFace) {
            case "backFace" -> {
                front = backFace;
                back = frontFace;
                left = rightFace;
                right = leftFace;
                up = turnFace(upFace, "down");
                bottom = turnFace(bottomFace, "down");
            }
            case "leftFace" -> {
                front = leftFace;
                back = rightFace;
                left = backFace;
                right = frontFace;
                up = turnFace(upFace, "right");
                bottom = turnFace(bottomFace, "left");
            }
            case "rightFace" -> {
                front = rightFace;
                back = leftFace;
                left = frontFace;
                right = backFace;
                up = turnFace(upFace, "left");
                bottom = turnFace(bottomFace, "right");
            }
            case "upFace" -> {
                front = upFace;
                back = turnFace(bottomFace, "down");
                left = turnFace(leftFace, "left");
                right = turnFace(rightFace, "right");
                up = turnFace(backFace, "down");
                bottom = frontFace;
            }
            case "bottomFace" -> {
                front = bottomFace;
                back = turnFace(upFace, "down");
                left = turnFace(leftFace, "right");
                right = turnFace(rightFace, "left");
                up = frontFace;
                bottom = turnFace(backFace, "down");
            }
        }

        assert direction.equals("clockwise") || direction.equals("counterClockwise");
        return turnCube(targetFace, direction, front, back, left, right, up, bottom);
    }

    // change tiles as required by the turning direction
    private Cube turnCube(String targetFace, String direction, Face front, Face back, Face left, Face right, Face up, Face bottom) {

        // 1-front
        Face f1;
        if (direction.equals("clockwise")) {
            f1 = turnFace(front, "left");
        } else {
            f1 = turnFace(front, "right");
        }

        // 2-back
        Face f2 = back;

        // 3-left
        Color[] face = new Color[9];
        System.arraycopy(left.wholeFace, 0, face, 0, 9);
        if (direction.equals("clockwise")) {
            face[2] = bottom.wholeFace[0];
            face[5] = bottom.wholeFace[1];
            face[8] = bottom.wholeFace[2];
        } else {
            face[2] = up.wholeFace[8];
            face[5] = up.wholeFace[7];
            face[8] = up.wholeFace[6];
        }
        Face f3 = new Face(face);

        // 4-right
        System.arraycopy(right.wholeFace, 0, face, 0, 9);
        if (direction.equals("clockwise")) {
            face[0] = up.wholeFace[6];
            face[3] = up.wholeFace[7];
            face[6] = up.wholeFace[8];
        } else {
            face[0] = bottom.wholeFace[2];
            face[3] = bottom.wholeFace[1];
            face[6] = bottom.wholeFace[0];
        }
        Face f4 = new Face(face);

        // 5-up
        System.arraycopy(up.wholeFace, 0, face, 0, 9);
        if (direction.equals("clockwise")) {
            face[6] = left.wholeFace[8];
            face[7] = left.wholeFace[5];
            face[8] = left.wholeFace[2];
        } else {
            face[6] = right.wholeFace[0];
            face[7] = right.wholeFace[3];
            face[8] = right.wholeFace[6];
        }
        Face f5 = new Face(face);

        // 6-bottom
        System.arraycopy(bottom.wholeFace, 0, face, 0, 9);
        if (direction.equals("clockwise")) {
            face[0] = right.wholeFace[6];
            face[1] = right.wholeFace[3];
            face[2] = right.wholeFace[0];
        } else {
            face[0] = right.wholeFace[2];
            face[1] = right.wholeFace[5];
            face[2] = right.wholeFace[8];
        }
        Face f6 = new Face(face);

        return turnCube(targetFace, f1, f2, f3, f4, f5, f6);
    }

    // position cube back to starting orientation
    private Cube turnCube(String targetFace, Face front, Face back, Face left, Face right, Face up, Face bottom) {
        switch (targetFace) {
            case "frontFace" -> {
                return new Cube(new Face[]{front, back, left, right, up, bottom});
            }
            case "backFace" -> {
                up = turnFace(up, "down");
                bottom = turnFace(bottom, "down");
                return new Cube(new Face[]{back, front, right, left, up, bottom});
            }
            case "leftFace" -> {
                up = turnFace(up, "left");
                bottom = turnFace(bottom, "right");
                return new Cube(new Face[]{right, left, front, back, up, bottom});
            }
            case "rightFace" -> {
                up = turnFace(up, "right");
                bottom = turnFace(bottom, "left");
                return new Cube(new Face[]{left, right, back, front, up, bottom});
            }
            case "upFace" -> {
                up = turnFace(up, "down");
                left = turnFace(left, "right");
                right = turnFace(right, "left");
                back = turnFace(back, "down");
                return new Cube(new Face[]{bottom, up, left, right, front, back});
            }
            default -> {    // targetFace = "bottomFace"
                bottom = turnFace(bottom, "down");
                left = turnFace(left, "left");
                right = turnFace(right, "right");
                back = turnFace(back, "down");
                return new Cube(new Face[]{up, bottom, left, right, back, front});
            }
        }
    }

    private Face turnFace(Face face, String newTop) {
        assert newTop.equals("left") || newTop.equals("right") || newTop.equals("down");
        switch (newTop) {
            case "left" -> {
                return new Face(face.center, face.left, face.top, face.right, face.down);
            }
            case "right" -> {
                return new Face(face.center, face.right, face.down, face.left, face.top);
            }
            default -> {
                return new Face(face.center, face.down, face.left, face.top, face.right);
            }
        }
    }

    public static Cube solved() {
        Color[][] tiles = new Color[6][9];
        for (int i = 0; i < 9; i++) {
            tiles[0][i] = Color.RED;
            tiles[1][i] = Color.ORANGE;
            tiles[2][i] = Color.YELLOW;
            tiles[3][i] = Color.GREEN;
            tiles[4][i] = Color.BLUE;
            tiles[5][i] = Color.WHITE;
        }
        Face[] faces = new Cube.Face[6];
        for (int i = 0; i < 6; i++) {
            faces[i] = new Face(tiles[i]);
        }
        return new Cube(faces);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cube cube = (Cube) o;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                if (faces[i].wholeFace[j] != cube.faces[i].wholeFace[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frontFace, backFace, leftFace, rightFace, upFace, bottomFace);
    }
}
