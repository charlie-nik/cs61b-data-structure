package byow.Core;

import byow.BuildingBlock.Area;
import byow.BuildingBlock.Position;
import byow.Graph.AStarSolver;
import byow.Graph.EnemyGraph;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.*;

/**
 * The primary class for game playing. A Game instance starts at level 0, keeps track of the
 * current game level, and builds a new world every time a new level is launched. Every world has
 * an avatar, a door, and three flowers as its basic setup. In order to win the game, player
 * collects all three flowers in order to unlock the door, and then passes through the door. A
 * world may also have a teleport or hostile NPCs, depending on the game level. All game objects
 * are initialized at random, unique positions.
 *      Level 1: 3 flowers
 *      Level 2: 3 flowers, 1 teleport
 *      Level 3: 3 flowers, 1 teleport, 1 enemy (1 move per second)
 *      Level 4: 3 flowers, 1 teleport, 1 enemy (1 move per 3/4 second)
 *      Level 5: 3 flowers, 1 teleport, 3 enemy (1 move per 3/4 second)
 *      level 6: 3 flowers, 1 teleport, 3 enemy (1 move per 1/2 second)
 *      level 7: 3 flowers, 1 teleport, 3 enemy (1 move per 1/2 second), limited vision
 *
 * The game class is also responsible for monitoring and executing the movement of avatar by
 * accordingly updating the world. Avatar is free to move to all FLOOR tiles and all passable
 * objects (flowers, locked/unlocked door, teleport entry & exit), although special procedures are
 * triggered when moved to certain tiles. See class for details.
 *
 * The game class is responsible for executing the movement of hostile NPCs as well. At every
 * step, NPCs move according to A* algorithm which calculates the shortest path from them to
 * avatar. NPCs can move through all passable objects, too. Once an NPC catches the avatar, the
 * game is lost.
 */
public class Game {
    private Random RANDOM;                              // a random specific to the current level
    protected TETile[][] WORLD;                         // a world specific to the current level
    protected int level;                                // current game level
    protected int gameState;                            // 0 == game on, 1 == won, -1 == lost
    protected int remainingFlowers;                     // number of flowers to still be collected

    private HashMap<Position, TETile> passableObjects;  // things that avatar & NPC can walk through
    private Position avatar;
    private Position door;
    private Position teleportExit;
    private List<Position> enemies;                     // list of hostile NPCs
    private EnemyGraph enemyGraph;                      // a graph of enemies's possible paths
    private Stopwatch enemyStopwatch;                   // enemies operate on an independent time
    private double enemyLastMove;                       // the time of enemies's last move

    /**
     * The Game constructor takes in a seed and saves it as its unique Random object. Every Game
     * instance starts at level 0.
     */
    public Game(long seed) {
        RANDOM = new Random(seed);
        WORLD = new TETile[Engine.WIDTH][Engine.HEIGHT];
        level = 0;
    }

    /**
     * Every time a new level is launched, randomly generates a new world. There are two
     * steps: first, use WorldGenerator to create a new world; second, initialize game objects
     * according to the game level. Since RANDOM is deterministically random, refreshes RANDOM
     * when necessary.
     */
    protected void buildNewWorld() {
        for (int i = 1; i < level; i++) {
            refreshRandom();
        }

        WorldGenerator god = new WorldGenerator(Engine.WIDTH, Engine.HEIGHT, RANDOM);
        while (!god.isWorldGenerated()) {
            Area.AREAS.clear();
            Area.ROOMS.clear();
            god.generateWorld();

            if (!god.isWorldGenerated()) {
                UI.worldGenerationFailureWindow();
                refreshRandom();
                god = new WorldGenerator(Engine.WIDTH, Engine.HEIGHT, RANDOM);
            }
        }

        WORLD = god.WORLD;
        gameState = 0;
        remainingFlowers = 3;
        switch (level) {
            case 1       -> setObjects(3, 0, 0);
            case 2       -> setObjects(3, 1, 0);
            case 3, 4    -> setObjects(3, 1, 1);
            case 5, 6, 7 -> setObjects(3, 1, 3);
        }
    }

    /**
     * Uses the current Random object to create a seed for constructing a new Random object.
     */
    private void refreshRandom() {
        long newSeed = RANDOM.nextInt();
        RANDOM = new Random(newSeed);
    }

    /**
     * Initializes game objects.
     */
    private void setObjects(int flowers, int teleport, int enemy) {
        passableObjects = new HashMap<>();

        // avatar
        initializeObject("avatar", Tileset.AVATAR);
        // door
        initializeObject("door", Tileset.LOCKED_DOOR);
        // flower
        for (int i = 0; i < flowers; i++) {
            initializeObject("flower", Tileset.FLOWER);
        }
        // teleport
        if (teleport > 0) {
            initializeObject("teleportEntry", Tileset.TELEPORT_ENTRY);
            initializeObject("teleportExit", Tileset.TELEPORT_EXIT);
        }
        // enemy
        if (enemy > 0) {
            enemies = new LinkedList<>();
            for (int i = 0; i < enemy; i++) {
                initializeObject("enemy", Tileset.ENEMY);
            }
            enemyGraph = new EnemyGraph(WORLD, passableObjects);
            enemyStopwatch = new Stopwatch();
            enemyLastMove = enemyStopwatch.elapsedTime();
        }
    }

    /**
     * Finds a random, unique position for each game object, and changes the tile at that
     * position to corresponding tile. If object is not avatar or enemy, adds it to the container
     * for passable objects. Adds enemy object to the list of enemies.
     */
    private void initializeObject(String object, TETile tile) {
        Position pos = randomPosition();
        changeTile(pos, tile);
        switch (object) {
            case "avatar" -> avatar = pos;
            case "door" -> door = pos;
            case "teleportExit" -> teleportExit = pos;
        }
        if (object.equals("enemy")) {
            enemies.add(pos);
        } else if (!object.equals("avatar")) {
            passableObjects.put(pos, tile);
        }
    }

    /**
     * Returns a random position that is not already occupied by existing game objects.
     */
    private Position randomPosition() {
        int x = RANDOM.nextInt(Engine.WIDTH - 8) + 4;
        int y = RANDOM.nextInt(Engine.HEIGHT - 8) + 4;
        if (WORLD[x][y] == Tileset.FLOOR) {
            return new Position(x, y);
        } else {
            return randomPosition();
        }
    }

    /**
     * For the first 6 levels, player can see the entire world. But for the final 7th level,
     * player's vision is limited. This is the primary method where Game instance communicates to
     * Engine what world should be rendered.
     */
    protected TETile[][] gameWorld() {
        if (level < 7) {
            return WORLD;
        } else {
            return worldWithinSight();
        }
    }

    /**
     * Returns the world within 8 unit-distance from the avatar.
     */
    private TETile[][] worldWithinSight() {
        int viewLimit = 8;
        int xLeft = avatar.getX() - viewLimit;
        int xRight = avatar.getX() + viewLimit;
        int yUp = avatar.getY() + viewLimit;
        int yBottom = avatar.getY() - viewLimit;

        TETile[][] limitedView = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int i = 0; i < Engine.WIDTH; i++) {
            for (int j = 0; j < Engine.HEIGHT; j++) {
                if (i >= xLeft && i <= xRight && j <= yUp && j >= yBottom) {
                    limitedView[i][j] = WORLD[i][j];
                } else {
                    limitedView[i][j] = Tileset.NOTHING;
                }
            }
        }
        return limitedView;
    }

    /**
     * This is the primary method where Engine communicates to Game how it should move the avatar.
     */
    protected void play(char key) {
        switch (Character.toUpperCase(key)) {
            case 'D' -> moveAvatar(avatar.east());
            case 'A' -> moveAvatar(avatar.west());
            case 'W' -> moveAvatar(avatar.north());
            case 'S' -> moveAvatar(avatar.south());
        }
    }

    /**
     * Determines what actions should be executed. If the intended position is WALL, no action is
     * made. Otherwise, moves the avatar according to the following rules:
     *     - For the current position:
     *          - If it's occupied by a passable object, through which AVATAR is currently
     *            passing, don't change it;
     *          - Else if it's plain AVATAR, replaces it with FLOOR.
     *     - For the intended position:
     *          - If it's occupied by a passable object, don't change it;
     *          - Else if it's FLOOR, replaces it with AVATAR.
     *     - Then, there's special operations to be executed for certain cases:
     *          - If the intended position is UNLOCKED DOOR, sets game state to 1;
     *          - If the intended position if FLOWER, decrements the number of remaining wild
     *            flowers by 1 and removes that position from the list of passable objects;
     *          - If the intended position is TELEPORT ENTRY, sends avatar to teleport exit.
     */
    private void moveAvatar(Position nextPos) {
        TETile nextTile = WORLD[nextPos.getX()][nextPos.getY()];
        if (isWalkable(nextTile)) {
            if (nextTile == Tileset.FLOWER) {
                remainingFlowers -= 1;
                passableObjects.remove(nextPos);
                if (remainingFlowers == 0) {
                    passableObjects.put(door, Tileset.UNLOCKED_DOOR);
                    changeTile(door, Tileset.UNLOCKED_DOOR);
                }
            }
            changeTile(avatar, passableObjects.getOrDefault(avatar, Tileset.FLOOR));
            changeTile(nextPos, passableObjects.getOrDefault(nextPos, Tileset.AVATAR));
            avatar = nextPos;

            if (nextTile == Tileset.UNLOCKED_DOOR) {
                gameState = 1;
            }
            if (nextTile == Tileset.TELEPORT_ENTRY) {
                avatar = teleportExit;
            }
        }
    }

    /**
     * Returns true if the given tile is either FLOOR or passable objects.
     */
    private boolean isWalkable(TETile tile) {
        return tile == Tileset.FLOOR || passableObjects.containsValue(tile);
    }

    /**
     * Replaces the input position with the input tile.
     */
    private void changeTile(Position pos, TETile tile) {
        WORLD[pos.getX()][pos.getY()] = tile;
    }

    /**
     * The primary method for NPCs' movement. The length of pause between each move is determined
     * by the game level: the higher, the faster. Uses A* algorithm to calculate the shortest
     * route for each enemy, and moves it to the next position. If the next position is avatar,
     * that is, if an enemy catches avatar, the game is over.
     */
    protected void enemyChase() {
        if (enemies == null) {
            return;
        }
        double interval = 0;
        switch (level) {
            case 3    -> interval = 1;
            case 4, 5 -> interval = 0.75;
            case 6, 7 -> interval = 0.5;
        }
        if (enemyStopwatch.elapsedTime() - enemyLastMove >= interval) {
            List<Position> enemiesMoved = new LinkedList<>();
            for (Position enemy : enemies) {
                AStarSolver aStar = new AStarSolver(enemyGraph, enemy, avatar);
                if (aStar.foundShortestPath()) {
                    Position nextPos = aStar.shortestPath().get(0);
                    changeTile(nextPos, passableObjects.getOrDefault(nextPos, Tileset.ENEMY));
                    changeTile(enemy, passableObjects.getOrDefault(enemy, Tileset.FLOOR));
                    if (nextPos.equals(avatar)) {
                        gameState = -1;
                        break;
                    }
                    enemiesMoved.add(nextPos);
                } else {
                    enemiesMoved.add(enemy);
                }
            }
            enemyLastMove = enemyStopwatch.elapsedTime();
            enemies = enemiesMoved;
        }
    }

    /**
     * For testing.
     */
    public static void main(String[] args) {
        Game g = new Game(8);
        g.buildNewWorld();
        g.enemyChase();
    }

}
