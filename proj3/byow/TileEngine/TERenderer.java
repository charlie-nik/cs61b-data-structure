package byow.TileEngine;

import byow.Core.Engine;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset. Shows head-up display if input level and flowers have real value.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world  the 2D TETile[][] array to render
     * @param level  the current game level (for non-game scenario, this will be -1)
     * @param flowers  the number of remaining wild flowers (for non-game scenario, this will be -1)
     */
    public void renderFrame(TETile[][] world, int level, int flowers) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
        if (!(level < 0 || flowers < 0)) {
            headUpDisplay(world, level, flowers);
        }
        StdDraw.show();
    }

    /**
     * Head-up display (HUD): description of the moused-over tile in the upper left corner,
     * current level and number of remaining wild flowers in the lower left corner, a general
     * message in the upper middle, and lastly the control menu in the upper right corner.
     */
    public void headUpDisplay(TETile[][] world, int level, int flowers) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        String tileDescription = "";
        if (x < Engine.WIDTH && y < Engine.HEIGHT) {
            tileDescription = "You see: " + world[x][y].description();
        }
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monoco", Font.BOLD, 16));
        StdDraw.textLeft(1.5, 1.5, "LEVEL  " + level);
        StdDraw.setFont(new Font("Monoco", Font.PLAIN, 16));
        StdDraw.textLeft(7.1, 1.5, "wild flowers: " + flowers + " " + "/ 3");
        StdDraw.textLeft(1.5, 28.5, tileDescription);
        StdDraw.textRight(Engine.WIDTH - 2, 28.5, "Controls (C)");

        switch (level) {
            case 1 -> StdDraw.text((float) Engine.WIDTH / 2, 28.5,
                    "Mouse over to read object's description.");
            case 2 -> StdDraw.text((float) Engine.WIDTH / 2, 28.5,
                    "Press (T) to de-activate teleport. Press again to re-activate.");
            case 3 -> StdDraw.text((float) Engine.WIDTH / 2, 28.5,
                    "Press (P) to show NPC's projected path. Press again to hide.");
            default -> {
                StdDraw.textRight((float) Engine.WIDTH / 2, 28.5, "Teleport (T)    ");
                StdDraw.textLeft((float) Engine.WIDTH / 2, 28.5, "    NPC's Path (P)");
                StdDraw.setFont(new Font("Monoco", Font.BOLD, 18));
                StdDraw.text((float) Engine.WIDTH / 2, 28.5, "||");
            }
        }
    }

    /**
     * The general Method for 2D tile-based world rendering. Never called in BYOW, just in
     * labs and demos.
     * @param world  the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        renderFrame(world, -1, -1);
    }

}
