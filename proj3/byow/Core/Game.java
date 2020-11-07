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
 * The primary class for game playing. A Game instance has a randomly generated world, within
 * which there's an avatar, a locked door, and three flowers, all initialized at random positions.
 * Game class monitors the movement of the avatar and updates the world accordingly.
 * FIXME doc game level
 */
public class Game {
    private Random RANDOM;                              // a random specific to the current level
    protected TETile[][] WORLD;                         // a world specific to the current level
    protected int level;                                // current game level
    protected int remainingFlowers;                     // number of flowers to still be collected
    protected int gameState;                            // 0 == game on, 1 == won, -1 == lost

    private Position avatar;
    private Position door;
    private Position teleportExit;

    private List<Position> enemies;                     // list of enemies
    private Stopwatch enemyStopwatch;                   // enemies operate on an independent time
    private double enemyLastMove;                       // the time of enemies's last move
    private EnemyGraph graph;                           // a graph of enemies's possible paths
    private HashMap<Position, TETile> passableObjects;  // a map of objects enemies can walk over
                                                        // when calculating shortest paths

    /**
     * Constructs a Game instance by generating a uniquely random world and initializing all game
     * objects (ie. avatar, door, flowers).
     */
    public Game(long seed) {
        RANDOM = new Random(seed);
        WORLD = new TETile[Engine.WIDTH][Engine.HEIGHT];
        level = 0;
    }

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
                refreshRandom();  // try for a new seed
                god = new WorldGenerator(Engine.WIDTH, Engine.HEIGHT, RANDOM);
            }
        }
        WORLD = god.WORLD;
        remainingFlowers = 3;
        gameState = 0;
        switch (level) {
            case 1       -> setObjects(3, 0, 0);
            case 2       -> setObjects(3, 1, 0);
            case 3, 4    -> setObjects(3, 1, 1);
            case 5, 6, 7 -> setObjects(3, 1, 3);
        }
    }

    private void refreshRandom() {
        long newSeed = RANDOM.nextInt();
        RANDOM = new Random(newSeed);
    }

    protected TETile[][] gameWorld() {
        return switch (level) {
            case 1, 2, 3, 4, 5, 6 -> WORLD;
            case 7                -> worldWithinSight();
            default               -> null;
        };
    }

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
     * Initializes game objects each to a random position and draws them onto the world.
     */
    private void setObjects(int flowers, int teleport, int enemy) {
        passableObjects = new HashMap<>();

        // avatar
        initializeObject("avatar", Tileset.AVATAR);

        // door
        initializeObject("door", Tileset.LOCKED_DOOR);

        // flowers
        for (int i = 0; i < flowers; i++) {
            initializeObject("flower", Tileset.FLOWER);
        }

        // teleport
        if (teleport > 0) {
            initializeObject("teleportEntry", Tileset.TELEPORT_ENTRY);
            initializeObject("teleportExit", Tileset.TELEPORT_EXIT);
        }

        // enemies
        if (enemy > 0) {
            enemies = new LinkedList<>();
            for (int i = 0; i < enemy; i++) {
                initializeObject("enemy", Tileset.ENEMY);
            }
            graph = new EnemyGraph(WORLD, passableObjects);
            enemyStopwatch = new Stopwatch();
            enemyLastMove = enemyStopwatch.elapsedTime();
        }
    }

    /**
     * FIXME doc
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
     * Returns a random floor position that is not already taken by existing game objects.
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
     * Moves the avatar as directed by the input key.
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
     * If the intended position is WALL, does nothing. Otherwise:
     *     - For the intended position:
     *          - If it's occupied by a passable object, don't change it;
     *          - Else if it's FLOOR, replaces it with AVATAR.
     *     - For the current position:
     *          - If it's occupied by a passable object, through which AVATAR is currently
     *            passing, don't change it;
     *          - Else if it's plain AVATAR, replaces it with FLOOR.
     *     - Then, there's some special operations to be executed for certain cases:
     *          - If the intended position is UNLOCKED DOOR, sets game state to 1;
     *          - If the intended position if FLOWER, decrements the number of remaining wild
     *            flowers by 1 and removes position from the list of passable objects;
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
     * Returns true if the given tile isn't WALL or occupied by NPC.
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

    // FIXME doc
    protected void enemyChase() {
        if (enemies == null) {
            return;
        }
        double interval = 0;
        switch (level) {
            case 3    -> interval = 1.00;
            case 4, 5 -> interval = 0.75;
            case 6, 7 -> interval = 0.50;  // enemies move faster and faster
        }
        if (enemyStopwatch.elapsedTime() - enemyLastMove >= interval) {
            List<Position> enemiesMoved = new LinkedList<>();
            for (Position enemy : enemies) {
                AStarSolver aStar = new AStarSolver(graph, enemy, avatar);
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
