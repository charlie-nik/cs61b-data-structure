package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.SpaceUtils.Position;

import java.util.Random;

public interface Area {
    Random RANDOM = new Random();
    int WIDTH = 80;
    int HEIGHT = 30;

    Position position();
    int width();
    int height();
    int numOfTiles();

    /**
     * Adds the given area to the world.
     */
    default void addToWorld(TETile[][] world) {
        TETile tile = TETile.colorVariant(Tileset.MOUNTAIN, 32, 32, 32, RANDOM);
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                int currX = position().getX() + x;
                int currY = position().getY() - y;

                world[currX][currY] = tile;
            }
        }
    }
}
