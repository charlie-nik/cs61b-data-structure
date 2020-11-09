package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.input.InputSource;
import byow.input.KeyboardInputSource;
import byow.input.StringInputSource;
import edu.princeton.cs.introcs.StdDraw;

import java.io.*;
import java.math.BigInteger;

/**
 * The primary class for user interaction. Accepting input from either keyboard or string entry,
 * this class processes the input key and, at its prompt, starts a new game, loads a previous
 * game, plays game, quits and saves the current game, or quits.
 */
public class Engine {
    private final TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int MENU_WIDTH = 30;
    public static final int MENU_HEIGHT = 30;
    private Game game;
    private boolean keyboard = true;
    private boolean almostQuitSave = false;

    /**
     * Method used for exploring a fresh world with keyboard input. One can also interact with
     * string for testing purposes.
     */
    public void interactWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();
        selectMainMenu(inputSource);
    }

    /**
     * According to the input key, performs one of the following actions (if input key does not
     * correspond to any of the options, ignores it and inspects the next key):
     *    - 'N': start a new game
     *    - 'L': load a previous game
     *    - 'Q': quit
     */
    private void selectMainMenu(InputSource inputSource) {
        UI.mainMenuWindow(keyboard);
        char key = 0;
        while (inputSource.possibleNextKey()) {
            key = cleanedNextKey(inputSource);
            if ("NLQ".contains(String.valueOf(key))) {
                break;
            }
        }
        switch (key) {
            case 'N' -> {
                long seed = processSeed(inputSource);
                startNewGame(seed);
                playGame(inputSource);
            }
            case 'L' -> {
                loadGame(inputSource);
                playGame(inputSource);
            }
            case 'Q' -> quitGame();
        }
    }

    /**
     * Processes input keys to retrieve the seed. Makes sure seed value doesn't exceed Java's
     * maximum integer value (32-bit). For aesthetic reasons, forbids further entry if it already
     * has 35 integers (numbers flow out of display window).
     */
    private long processSeed(InputSource inputSource) {
        UI.enterSeedWindow("", keyboard);
        StringBuilder sb = new StringBuilder();
        while (inputSource.possibleNextKey()) {
            char key = cleanedNextKey(inputSource);
            if (Character.isDigit(key) && sb.length() < 36) {
                sb.append(key);
                UI.enterSeedWindow(sb.toString(), keyboard);
            } else if (key == 'S' && sb.length() > 0) {
                break;
            }
        }
        BigInteger input = new BigInteger(sb.toString());
        if (input.compareTo(new BigInteger("2147483647")) > 0) {
            return input.divideAndRemainder(new BigInteger("2147483647"))[1].intValue();
        } else {
            return input.intValue();
        }
    }

    /**
     * Generates a new Game instance using the input seed and initializes a new game history.
     */
    private void startNewGame(long seed) {
        ter.initialize(WIDTH, HEIGHT);
        game = new Game(seed);
        newLevel();
    }

    /**
     * Generates a new world for each new level and displays a level intro window.
     */
    private void newLevel() {
        game.level += 1;
        UI.levelTitleWindow(game.level, keyboard);
        game.buildNewWorld();
        ter.renderFrame(game.renderWorld(), game.level, game.remainingFlowers);
    }

    /**
     * Repeats playing game and updating game state with TERender as the avatar moves around,
     * until the next input key asks to quit or quit-save (this is not being monitored here but
     * in cleanedNextKey() method), or until player wins or loses the game. Opens a control window
     * when player enters 'C'.
     */
    private void playGame(InputSource inputSource) {
        while (true) {
            while (inputSource.possibleNextKey()) {
                if (StdDraw.hasNextKeyTyped()) {
                    char key = cleanedNextKey(inputSource);
                    switch (key) {
                        case 'W', 'S', 'A', 'D' -> game.play(key);
                        case 'T' -> game.teleportSwitch();
                        case 'P' -> game.enemyPathSwitch();
                        case 'C' -> showControlWindow(inputSource);
                    }
                }
                game.enemyChase();
                ter.renderFrame(game.renderWorld(), game.level, game.remainingFlowers);

                if (game.gameState != 0) {
                    StdDraw.pause(200);
                    UI.levelResultWindow(game.level, game.gameState, keyboard);
                    break;
                }
            }
            if (game.level < 7) {
                newLevel();
            } else {
                break;
            }
        }
        StdDraw.pause(1000);
        System.exit(0);
    }

    /**
     * Opens a window displaying control options and instructions. 'C' stands for 'continue,' 'M'
     * stands for 'main menu,' and player can also quit or quit-save.
     */
    private void showControlWindow(InputSource inputSource) {
        UI.controlWindow(game.level, keyboard);
        while (true) {
            char key = cleanedNextKey(inputSource);
            if (key == 'C') {
                return;
            } else if (key == 'M') {
                selectMainMenu(inputSource);
            }
        }
    }

    /**
     * Saves the Game object to file.
     */
    private void saveGame() {
        File file = new File("./save_data.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /**
     * Loads Game object from file. If file doesn't exist, opens an error window and returns to
     * main menu.
     */
    private void loadGame(InputSource inputSource) {
        File file = new File("./save_data.txt");
        if (!file.exists()) {
            UI.loadFailureWindow(keyboard);
            selectMainMenu(inputSource);
            return;
        }
        try {
            FileInputStream fos = new FileInputStream(file);
            ObjectInputStream oos = new ObjectInputStream(fos);
            game = (Game) oos.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
            System.exit(0);
        }
        ter.initialize(WIDTH, HEIGHT);
    }

    /**
     * Immediately quits the game without asking user for further confirmation.
     */
    private void quitGame() {
        System.exit(0);
    }

    /**
     * Immediately saves and quits the game.
     */
    private void quitSaveGame() {
        saveGame();
        quitGame();
    }

    /**
     * Returns the next key in uppercase unless it asks to quit ('Q') or quit-save (':Q') the game.
     */
    private char cleanedNextKey(InputSource inputSource) {
        char key = Character.toUpperCase(inputSource.getNextKey());
        if (key == ':') {
            almostQuitSave = true;
        } else if (almostQuitSave) {
            if (key == 'Q') {
                quitSaveGame();
            } else {
                almostQuitSave = false;
            }
        } else if (key == 'Q') {
            quitGame();
        }
        return key;
    }

    /**
     * Method used for testing the code. Returns the final state of the world.
     */
    public TETile[][] interactWithInputString(String input) {
        keyboard = false;
        InputSource inputSource = new StringInputSource(input);
        selectMainMenu(inputSource);

        return game.WORLD;
    }

    /**
     * For testing.
     */
    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.interactWithInputString("lddwwasss");
    }

}
