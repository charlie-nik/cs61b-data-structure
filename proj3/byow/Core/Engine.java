package byow.Core;

import byow.Input.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.*;
import java.math.BigInteger;

/**
 * The primary class for user interaction. Accepting input from either keyboard or string entry,
 * this class processes the input key and, at its prompt, starts a new game, loads a previous
 * game, quits and saves the current game, or quits.
 *
 * FIXME: game saving/loading is fucked after adding multiple levels
 */
public class Engine {
    private final TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int MENU_WIDTH = 30;
    public static final int MENU_HEIGHT = 30;
    private Game game;
    private StringBuilder gameHistory;
    private boolean keyboard;

    /**
     * Method used for exploring a fresh world with keyboard input. One can also interact with
     * string for the purpose of testing.
     */
    public void interactWithKeyboard() {
        keyboard = true;
        InputSource inputSource = new KeyboardInputSource();
        selectMainMenu(inputSource);
    }

    /**
     * According to the input key, performs one of the following options (if input key does not
     * correspond to any of the options, ignores it and inspects the next):
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
                playGame(inputSource, true);
            }
            case 'L' -> {
                loadGame(inputSource);
                playGame(inputSource, true);
            }
            case 'Q' -> quitGame();
        }
    }

    /**
     * Generates a new Game instance using the input seed and displays the world onscreen.
     */
    private void startNewGame(long seed) {
        game = new Game(seed);
        gameHistory = new StringBuilder();
        gameHistory.append(seed).append('S');

        ter.initialize(WIDTH, HEIGHT);
    }

    /**
     * Repeats playing game and updating game state with TERender as the avatar moves around -
     * until the next input key asks to quit or quit-save. This is not being monitored here but
     * in cleanedNextKey() method.
     */
    private void playGame(InputSource inputSource, boolean gameOn) {
        while (true) {
            newLevel();
            while (inputSource.possibleNextKey()) {
                UI.headUpDisplay(game.gameWorld(), game.remainingFlowers, keyboard);
                if (StdDraw.hasNextKeyTyped()) {
                    char key = cleanedNextKey(inputSource);
                    game.play(key);
                    gameHistory.append(key);
                }
                game.enemyChase();
                ter.renderFrame(game.gameWorld());

                if (game.gameState != 0) {
                    StdDraw.pause(500);
                    UI.levelResultWindow(game.level, game.gameState, keyboard);
                    break;
                }
            }
        }
    }

    // FIXME doc
    private void newLevel() {
        game.level += 1;
        UI.levelWindow(game.level, keyboard);
        game.buildNewWorld();
        ter.renderFrame(game.gameWorld());
    }

    /**
     * Saves the String value of gameHistory to file.
     */
    private void saveGame() {
        File file = new File("./save_data.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameHistory.toString());
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /**
     * Loads gameHistory from file. If file doesn't exist, opens a notification window and then
     * returns to main menu.
     * FIXME: multiple levels fuck-up
     */
    private void loadGame(InputSource inputSource) {
        File file = new File("./save_data.txt");
        if (!file.exists()) {
            UI.loadFailureWindow(keyboard);
            selectMainMenu(inputSource);
            return;
        }
        String lastGame = null;
        try {
            FileInputStream fos = new FileInputStream(file);
            ObjectInputStream oos = new ObjectInputStream(fos);
            lastGame = (String) oos.readObject();
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
        InputSource lastInput = new StringInputSource(lastGame);
        long seed = processSeed(lastInput);
        startNewGame(seed);
        playGame(lastInput, false);
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
     * If that's the case, returns 0 (ie. null).
     */
    private char cleanedNextKey(InputSource inputSource) {
        char key = Character.toUpperCase(inputSource.getNextKey());
        if (key == ':' && inputSource.possibleNextKey()) {
            char nextKey = Character.toUpperCase(inputSource.getNextKey());
            if (nextKey == 'Q') {
                quitSaveGame();
            }
        } else if (key == 'Q') {
            quitGame();
        } else {
            return key;
        }
        return 0;
    }

    /**
     * Looks at the next input key and returns it if it's a valid seed input or if it's 'S'.
     * Otherwise, returns 0 (ie. null).
     */
    private char enterSeed(InputSource inputSource) {
        if (inputSource.possibleNextKey()) {
            char key = cleanedNextKey(inputSource);
            if (Character.isDigit(key) || key == 'S') {
                return key;
            }
        }
        return 0;
    }

    /**
     * Processes input keys to get the seed. Makes sure seed value doesn't exceed Java's maximum
     * integer value (32-bit).
     * FIXME 35 limit
     */
    private long processSeed(InputSource inputSource) {
        UI.enterSeedWindow("", keyboard);
        StringBuilder sb = new StringBuilder();
        while (inputSource.possibleNextKey()) {
            char key = enterSeed(inputSource);
            if (Character.isDigit(key) && sb.length() < 35) {
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
        engine.interactWithInputString("n3sddasswawwa");
    }

}
