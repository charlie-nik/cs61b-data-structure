package byow.Core;

import byow.BuildingBlock.Area;
import byow.BuildingBlock.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;

import static byow.BuildingBlock.Area.AREAS;

/**
 * The primary class for game playing. A Game instance has a randomly generated world, within
 * which there's an avatar, a locked door, and three flowers, all initialized at random positions.
 * Game class monitors the movement of the avatar and updates the world accordingly.
 */
public class Game {
    private final Random RANDOM;
    protected final TETile[][] WORLD;
    private Position avatar;
    private Position door;
    protected int remainingFlowers;
    protected boolean gameWon;

    /**
     * Constructs a Game instance by generating a uniquely random world and initializing all game
     * objects (ie. avatar, door, flowers).
     */
    public Game(int width, int height, long seed) {
        RANDOM = new Random(seed);
        WORLD = new WorldGenerator(width, height, RANDOM).WORLD;
        remainingFlowers = 3;
        gameWon = false;
        setObjects();
    }

    /**
     * Initializes each game object to a random position and draws it onto the world.
     */
    private void setObjects() {
        HashSet<Position> takenPositions = new HashSet<>();

        avatar = randomPosition(takenPositions);
        changeTile(avatar, Tileset.AVATAR);

        door = randomPosition(takenPositions);
        changeTile(door, Tileset.LOCKED_DOOR);

        for (int i = 0; i < remainingFlowers; i++) {
            Position flower = randomPosition(takenPositions);
            changeTile(flower, Tileset.FLOWER);
        }
    }

    /**
     * Returns a random floor position that is not already taken by another game object.
     */
    private Position randomPosition(HashSet<Position> takenPositions) {
        Area area = AREAS.get(RANDOM.nextInt(AREAS.size()));
        int x = RANDOM.nextInt(area.width()) + area.position().getX();
        int y = area.position().getY() - RANDOM.nextInt(area.height());

        Position pos = new Position(x, y);
        if (takenPositions.contains(pos)) {
            return randomPosition(takenPositions);
        }
        takenPositions.add(pos);
        return pos;
    }

    /**
     * Moves the avatar as directed by the input key.
     */
    public void play(char key) {
        switch (Character.toUpperCase(key)) {
            case 'D' -> moveAvatar(avatar.east());
            case 'A' -> moveAvatar(avatar.west());
            case 'W' -> moveAvatar(avatar.north());
            case 'S' -> moveAvatar(avatar.south());
        }
    }

    /**
     * If the intended position is an unlocked door, the game is won. If the intended position is
     * neither floor nor flower, stays and does nothing. Otherwise, moves the avatar to the
     * intended position. If the intended position is flower, reduces the number of remaining
     * flowers by one. If there is no more remaining flowers, unlocks the locked door.
     */
    private void moveAvatar(Position nextPos) {
        if (isTile(nextPos, Tileset.UNLOCKED_DOOR)) {
            gameWon = true;
            return;
        }
        if (!(isTile(nextPos, Tileset.FLOOR) || isTile(nextPos, Tileset.FLOWER))) {
            return;
        }
        if (isTile(nextPos, Tileset.FLOWER)) {
            remainingFlowers -= 1;
            if (remainingFlowers == 0) {
                changeTile(door, Tileset.UNLOCKED_DOOR);
            }
        }
        changeTile(avatar, Tileset.FLOOR);
        changeTile(nextPos, Tileset.AVATAR);
        avatar = nextPos;
    }

    /**
     * Replaces the input position with the input tile.
     */
    private void changeTile(Position pos, TETile tile) {
        WORLD[pos.getX()][pos.getY()] = tile;
    }

    /**
     * Returns true if the input position has the input tile.
     */
    private boolean isTile(Position pos, TETile tile) {
        return WORLD[pos.getX()][pos.getY()] == tile;
    }

}
