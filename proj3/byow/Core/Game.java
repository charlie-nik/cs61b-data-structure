package byow.Core;

import byow.BuildingBlock.Area;
import byow.BuildingBlock.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

import static byow.BuildingBlock.Area.AREAS;

/**
 * A Game object that includes a world and an avatar.
 */
public class Game implements Serializable {
    private final Random RANDOM;
    protected final TETile[][] WORLD;
    private Position avatar;
    private TETile avatarTile = Tileset.AVATAR;

    private static final long serialVersionUID = -5348346723004273804L;

    /**
     * Constructs a Game instance by initiating a uniquely random world and an avatar using the
     * input seed.
     */
    public Game(int width, int height, long seed) {
        RANDOM = new Random(seed);
        WORLD = new WorldGenerator(width, height, RANDOM).WORLD;
        avatar = initiateAvatarPosition();
        drawAvatar();
    }

    /**
     * Moves the avatar as directed by the input key.
     */
    public void play(char key) {
        move(key);
    }

    /**
     * Returns a random position for avatar's initial appearance.
     */
    private Position initiateAvatarPosition() {
        Area area = AREAS.get(RANDOM.nextInt(AREAS.size()));
        int x = RANDOM.nextInt(area.width()) + area.position().getX();
        int y = area.position().getY() - RANDOM.nextInt(area.height());
        return new Position(x, y);
    }

    /**
     * Changes avatar's position to AVATAR tile.
     */
    private void drawAvatar() {
        WORLD[avatar.getX()][avatar.getY()] = avatarTile;
    }

    /**
     * Interprets input key and directs avatar to the corresponding direction.
     */
    public void move(char key) {
        switch (Character.toUpperCase(key)) {
            case 'D' -> moveAvatar(avatar.east());
            case 'A' -> moveAvatar(avatar.west());
            case 'W' -> moveAvatar(avatar.north());
            case 'S' -> moveAvatar(avatar.south());
        }
    }

    /**
     * If the intended position is not a wall, moves avatar to it and updates both its new and old
     * positions to AVATAR and FLOOR tiles, respectively.
     */
    private void moveAvatar(Position nextPos) {
        if (isWalkable(nextPos)) {
            WORLD[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
            avatar = nextPos;
            drawAvatar();
        }
    }

    /**
     * Checks if the input position is floor.
     */
    private boolean isWalkable(Position pos) {
        return WORLD[pos.getX()][pos.getY()] == Tileset.FLOOR;
    }
}
