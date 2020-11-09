package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.architect.Area;
import byow.architect.Position;
import byow.graph.AStarSolver;
import byow.graph.EnemyGraph;
import edu.princeton.cs.introcs.Stopwatch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
 *      Level 4: 3 flowers, 1 teleport, 1 enemy (1 move per 0.8 second)
 *      Level 5: 3 flowers, 1 teleport, 3 enemy (1 move per 0.8 second)
 *      level 6: 3 flowers, 1 teleport, 3 enemy (1 move per 0.6 second)
 *      level 7: 3 flowers, 1 teleport, 3 enemy (1 move per 0.6 second), limited vision
 *
 * The game class is also responsible for monitoring and executing the movement of avatar by
 * accordingly updating the world. Avatar is free to move on all FLOOR tiles and all passable
 * objects (flowers, locked/unlocked door, teleport entry & exit), although special procedures are
 * triggered when moved to certain tiles. See class for details. When avatar is moving 'through' a
 * passable object, it can't be seen from the map - I mean, it's being 'through.'
 *
 * The game class is responsible for executing the movement of hostile NPCs as well. At every
 * step, NPCs move according to A* algorithm that calculates the shortest path from them to
 * avatar. NPCs can move through all passable objects, too. Once an NPC catches the avatar, the
 * game is lost.
 */
public class Game implements Serializable {
    private static final long serialVersionUID = 2121797155503707888L;

    private Random RANDOM;                              // a random specific to the current level
    protected TETile[][] WORLD;                         // a world specific to the current level
    protected TETile[][] surfaceWorld;                  // the world the player sees
    protected int level;                                // current game level
    protected int gameState;                            // 0 -> game on, 1 -> won, -1 -> lost
    protected int remainingFlowers;                     // number of flowers to still be collected

    private HashMap<Position, TETile> passableObjects;  // things that avatar & NPC can walk through
    private Position avatar;
    private Position door;
    private Position teleportEntry;
    private Position teleportExit;
    private List<Position> enemies;                     // list of hostile NPCs
    private EnemyGraph enemyGraph;                      // a graph of enemies's possible paths
    private EnemyStopwatch enemyStopwatch;              // to track interval between each move
    private double enemyLastMove;                       // the time of enemies's last move
    private HashMap<Position, List<Position>> enemyPaths;   // enemies' projected paths
    private boolean showEnemyPath;                      // display enemies's projected paths or not

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
     * Every time a new level is launched, randomly generates a new world in two steps: first,
     * uses WorldGenerator to create a new world; second, initializes game objects according to the
     * current game level. Since RANDOM is deterministically random, refreshes RANDOM for each
     * new level and when world generation exceeds time limit.
     */
    protected void buildNewWorld() {
        RANDOM = new Random(RANDOM.nextInt());
        WorldGenerator god = new WorldGenerator(Engine.WIDTH, Engine.HEIGHT, RANDOM);
        while (!god.isWorldGenerated()) {
            Area.AREAS.clear();
            Area.ROOMS.clear();
            god.generateWorld();
            if (!god.isWorldGenerated()) {
                UI.worldGenerationFailureWindow();
                RANDOM = new Random(RANDOM.nextInt());
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
            initializeObject("teleport entry", Tileset.TELEPORT_ENTRY);
            initializeObject("teleport exit", Tileset.TELEPORT_EXIT);
        }
        // enemy
        if (enemy > 0) {
            enemies = new LinkedList<>();
            for (int i = 0; i < enemy; i++) {
                initializeObject("enemy", Tileset.ENEMY);
            }
            enemyGraph = new EnemyGraph(WORLD, passableObjects);
            enemyStopwatch = new EnemyStopwatch();
            enemyLastMove = enemyStopwatch.elapsedTime();
            showEnemyPath = false;
        }
    }

    /**
     * Finds a random, unique position for each game object, and changes the tile at that
     * position to corresponding tile. For all objects that are neither avatar nor enemy, adds them
     * to passable objects. Adds enemy object to enemies.
     */
    private void initializeObject(String object, TETile tile) {
        Position pos = randomPosition();
        changeTile(pos, tile);
        switch (object) {
            case "avatar" -> avatar = pos;
            case "door" -> door = pos;
            case "teleport entry" -> teleportEntry = pos;
            case "teleport exit" -> teleportExit = pos;
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
        if (WORLD[x][y].equals(Tileset.FLOOR)) {
            return new Position(x, y);
        } else {
            return randomPosition();
        }
    }

    /**
     * For the first 6 levels, player can see the entire world. For the final 7th level, player's
     * vision is limited. Paints enemy's path with red floor tile if player chooses to show path.
     * This is the primary method where Game instance communicates to Engine what world should be
     * rendered.
     */
    protected TETile[][] renderWorld() {
        surfaceWorld = new TETile[Engine.WIDTH][Engine.HEIGHT];
        if (level < 7) {
            worldWithinSight(-1);
        } else {
            worldWithinSight(8);
        }
        if (showEnemyPath) {
            showEnemyPath();
        }
        return surfaceWorld;
    }

    /**
     * If view limit < 0, player can see the entire world. Otherwise, player can only see the
     * world within view limit, beyond which is nothingness.
     */
    private void worldWithinSight(int viewLimit) {
        int xLeft   = viewLimit < 0 ? 0 : avatar.getX() - viewLimit;
        int xRight  = viewLimit < 0 ? Engine.WIDTH - 1 : avatar.getX() + viewLimit;
        int yUp     = viewLimit < 0 ? Engine.HEIGHT - 1 : avatar.getY() + viewLimit;
        int yBottom = viewLimit < 0 ? 0: avatar.getY() - viewLimit;

        for (int i = 0; i < Engine.WIDTH; i++) {
            for (int j = 0; j < Engine.HEIGHT; j++) {
                if (i >= xLeft && i <= xRight && j <= yUp && j >= yBottom) {
                    surfaceWorld[i][j] = WORLD[i][j];
                } else {
                    surfaceWorld[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    /**
     * Replaces all tiles on enemy's projected path - except passable objects - with a red FLOOR
     * tile named ENEMY_PATH.
     */
    private void showEnemyPath() {
        for (List<Position> path : enemyPaths.values()) {
            for (Position pos : path) {
                if (surfaceWorld[pos.getX()][pos.getY()].equals(Tileset.FLOOR)) {
                    surfaceWorld[pos.getX()][pos.getY()] = Tileset.ENEMY_PATH;
                }
            }
        }
    }

    /**
     * If enemy's path is currently shown, hides it. Otherwise, shows it.
     */
    protected void enemyPathSwitch() {
        if (enemyPaths != null) {
            showEnemyPath = !showEnemyPath;
        }
    }

    /**
     * If teleport is currently active, de-activates it. Otherwise, re-activates it.
     */
    protected void teleportSwitch() {
        if (teleportEntry != null) {
            if (passableObjects.get(teleportEntry).equals(Tileset.TELEPORT_ENTRY)) {
                passableObjects.put(teleportEntry, Tileset.TELEPORT_ENTRY_DEACTIVATED);
                passableObjects.put(teleportExit, Tileset.TELEPORT_EXIT_DEACTIVATED);
            } else {
                passableObjects.put(teleportEntry, Tileset.TELEPORT_ENTRY);
                passableObjects.put(teleportExit, Tileset.TELEPORT_EXIT);
            }
            changeTile(teleportEntry, passableObjects.get(teleportEntry));
            changeTile(teleportExit, passableObjects.get(teleportExit));
        }
    }

    /**
     * This is the primary method where Engine communicates to Game how the player wants to move
     * the avatar.
     */
    protected void play(char key) {
        switch (key) {
            case 'D' -> moveAvatar(avatar.east());
            case 'A' -> moveAvatar(avatar.west());
            case 'W' -> moveAvatar(avatar.north());
            case 'S' -> moveAvatar(avatar.south());
        }
    }

    /**
     * Determines what actions should be executed when avatar moves. If the intended position is
     * WALL or ENEMY, no action is made. Otherwise, moves the avatar according to the following
     * rules:
     *  - If both intended position and current position are FLOOR, executes the simplest
     *    actions without going through the rest (which nonetheless already covers it). This
     *    is to save time and space at the expense of coding aesthetics.
     *  - Else:
     *      - If the intended position is FLOWER, decrements the number of remaining wild flowers
     *        by 1 and removes that position from the list of passable objects. If there's no
     *        more wild flowers, unlocks door.
     *      - For the current position:
     *          - If it's occupied by a passable object, through which AVATAR is currently
     *            passing, don't change it;
     *          - Else if it's plain AVATAR, replaces it with FLOOR.
     *      - For the intended position:
     *          - If it's occupied by a passable object, don't change it;
     *          - Else if it's FLOOR, replaces it with AVATAR.
     *      - Then, there's some more special operations to be executed under certain circumstances:
     *          - If the intended position is UNLOCKED DOOR, sets game state to 1;
     *          - If the intended position is TELEPORT ENTRY, sends avatar to teleport exit.
     */
    private void moveAvatar(Position nextPos) {
        TETile currTile = WORLD[avatar.getX()][avatar.getY()];
        TETile nextTile = WORLD[nextPos.getX()][nextPos.getY()];

        if (!nextTile.equals(Tileset.WALL) && !nextTile.equals(Tileset.ENEMY)) {
            if (currTile.equals(Tileset.AVATAR) && nextTile.equals(Tileset.FLOOR)) {
                changeTile(avatar, Tileset.FLOOR);
                changeTile(nextPos, Tileset.AVATAR);
                avatar = nextPos;
            } else {
                if (nextTile.equals(Tileset.FLOWER)) {
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
                if (nextTile.equals(Tileset.UNLOCKED_DOOR)) {
                    gameState = 1;
                }
                if (nextTile.equals(Tileset.TELEPORT_ENTRY)) {
                    avatar = teleportExit;
                }
            }
        }
    }

    /**
     * The primary method for NPCs' movement. The length of pause between each move is determined
     * by the game level: the higher, the shorter. Uses A* algorithm to calculate the shortest
     * route for each enemy and stores the route in list. If the time between now and NPCs' last
     * move has reached interval, moves NPC to the next position. Note that although a position
     * occupied by another NPC is treated as a valid movable point when calculating shortest
     * path for an NPC, lets it wait rather than 'merge' with another NPC. If the next position
     * is avatar, that is, if an NPC catches avatar, the game is over.
     */
    protected void enemyChase() {
        if (enemies == null) {
            return;
        }
        double interval = 0;
        switch (level) {
            case 3    -> interval = 1;
            case 4, 5 -> interval = 0.8;
            case 6, 7 -> interval = 0.6;
        }
        enemyPaths = new HashMap<>();
        for (Position enemy : enemies) {
            AStarSolver aStar = new AStarSolver(enemyGraph, enemy, avatar);
            if (aStar.foundShortestPath()) {
                enemyPaths.put(enemy, aStar.shortestPath());
            }
        }
        if (enemyStopwatch.elapsedTime() - enemyLastMove >= interval) {
            List<Position> enemiesMoved = new LinkedList<>();
            for (Position enemy : enemies) {
                if (enemyPaths.containsKey(enemy)) {
                    Position nextPos = enemyPaths.get(enemy).get(0);
                    if (!WORLD[nextPos.getX()][nextPos.getY()].equals(Tileset.ENEMY)) {
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
                } else {
                    enemiesMoved.add(enemy);
                }
            }
            enemyLastMove = enemyStopwatch.elapsedTime();
            enemies = enemiesMoved;
        }
    }

    /**
     * Replaces the input position with the input tile.
     */
    private void changeTile(Position pos, TETile tile) {
        WORLD[pos.getX()][pos.getY()] = tile;
    }

    /**
     * Because Princeton's original Stopwatch does not implement Serializable, this class is
     * created solely for the purpose of serialization.
     */
    private class EnemyStopwatch extends Stopwatch implements Serializable {
        private static final long serialVersionUID = 9166708656535522336L;
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
