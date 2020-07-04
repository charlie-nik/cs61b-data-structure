package bearmaps.hw4.rubikscube;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

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
        private Color[] wholeFace, top, right, down, left;
        private Color center;
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

    private final Face frontFace, backFace, leftFace, rightFace, upFace, bottomFace;

    public Cube(ArrayList<Face> tiles) {
        frontFace = tiles.get(0);
        backFace = tiles.get(1);
        leftFace = tiles.get(2);
        rightFace = tiles.get(3);
        upFace = tiles.get(4);
        bottomFace = tiles.get(5);
    }

    public Cube(List<Face> faces) {
        frontFace = faces.get(0);
        backFace = faces.get(1);
        leftFace = faces.get(2);
        rightFace = faces.get(3);
        upFace = faces.get(4);
        bottomFace = faces.get(5);
    }

    public static Cube readCube(String filename) {
        In in = new In(filename);
        String line = in.readLine();
        String[] tokens = line.trim().split("\\s+");

        ArrayList<Face> tiles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Color[] face = new Color[9];
            for (int j = 0; j < 9; j++) {
                String color = tokens[j];
                switch (color) {
                    case "red" -> face[j] = Color.RED;
                    case "orange" -> face[j] = Color.ORANGE;
                    case "yellow" -> face[j] = Color.YELLOW;
                    case "green" -> face[j] = Color.GREEN;
                    case "blue" -> face[j] = Color.BLUE;
                    default -> face[j] = Color.WHITE;
                }
            }
            tiles.add(new Face(face));
            line = in.readLine();
            tokens = line.trim().split("\\s+");
        }
        return new Cube(tiles);
    }

    public List<Cube> neighbors() {
        List<Cube> neighbors = new ArrayList<>();

        // 1-front face: turn clockwise

        // 1-front
        Face f1 = new Face(frontFace.center, frontFace.left, frontFace.top, frontFace.right, frontFace.down);
        // 2-back
        Face f2 = backFace;
        // 3-left
        Color[] face = new Color[9];
        System.arraycopy(leftFace.wholeFace, 0, face, 0, 9);
        face[2] = bottomFace.wholeFace[0];
        face[5] = bottomFace.wholeFace[1];
        face[8] = bottomFace.wholeFace[2];
        Face f3 = new Face(face);
        // 4-right
        System.arraycopy(rightFace.wholeFace, 0, face, 0, 9);
        face[0] = upFace.wholeFace[6];
        face[3] = upFace.wholeFace[7];
        face[6] = upFace.wholeFace[8];
        Face f4 = new Face(face);
        // 5-up
        System.arraycopy(upFace.wholeFace, 0, face, 0, 9);
        face[6] = leftFace.wholeFace[8];
        face[7] = leftFace.wholeFace[5];
        face[8] = leftFace.wholeFace[2];
        Face f5 = new Face(face);
        // 6-bottom
        System.arraycopy(bottomFace.wholeFace, 0, face, 0, 9);
        face[0] = rightFace.wholeFace[6];
        face[1] = rightFace.wholeFace[3];
        face[2] = rightFace.wholeFace[0];
        Face f6 = new Face(face);

        neighbors.add(new Cube(List.of(f1, f2, f3, f4, f5, f6)));
    }

    private Cube turnClockwise(String targetFace) {
        switch (targetFace) {
            case "frontFace": {
                Face front = frontFace;
                Face back = backFace;
                Face left = leftFace;
                Face right = rightFace;
                Face up = upFace;
                Face bottom = bottomFace;
                break;
            }
            case "bottomFace": {
                Face front = bottomFace;
                Face back = upFace;
                Face left = new Face(leftFace.center, leftFace.right, leftFace.down, leftFace.left, leftFace.top);
                Face right = new Face(rightFace.center, rightFace.left, rightFace.top, rightFace.right, rightFace.down);
                Face up = frontFace;
                Face bottom = backFace;
                break;
            }
            case "leftFace": {
                Face front = leftFace;
                Face back = rightFace;
                Face left = backFace;
                Face right = frontFace;
                Face up = new Face(upFace.center, upFace.right, upFace.down, upFace.left, upFace.top);
                Face bottom = new Face(bottomFace.center, bottomFace.left, bottomFace.top, bottomFace.right, bottomFace.down);
                break;
            }
            case "rightFace": {
                Face front = rightFace;
                Face back = leftFace;
                Face left = frontFace;
                Face right = backFace;
                Face up = new Face(upFace.center, upFace.left, upFace.top, upFace.right, upFace.down);
                Face bottom = new Face(bottomFace.center, bottomFace.right, bottomFace.down, bottomFace.left, bottomFace.top);
                break;
            }
            case "upFace": {
                Face front = upFace;
                Face back = bottomFace;
                Face left = leftFace;
                Face right = rightFace;
                Face up = back;
                Face bottom = bottomFace;
                break;
            }
        }
    }
}
