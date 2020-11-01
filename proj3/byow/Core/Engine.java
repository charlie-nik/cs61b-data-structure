package byow.Core;

import byow.Input.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;

/**
 * The primary class for user interaction. Accepting input from either keyboard or string entry,
 * this class processes the input key and, at its prompt, starts a new game, loads a previous
 * game, quits and saves the current game, or quits.
 */
public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int MENU_WIDTH = 30;
    private static final int MENU_HEIGHT = 30;
    private Game game;
    private StringBuilder gameHistory;
    private boolean keyboard;

    /**
     * Method used for exploring a fresh world with keyboard input.
     */
    public void interactWithKeyboard() {
        keyboard = true;
        InputSource inputSource = new KeyboardInputSource();
        selectMainMenu(inputSource);
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
     * According to the input key, performs one of the following options (if input key does not
     * correspond to any of the options, ignores it and inspects the next):
     *    - 'N': start a new game
     *    - 'L': load a previous game
     *    - 'Q': quit
     */
    private void selectMainMenu(InputSource inputSource) {
        mainMenuWindow();
        char key = 0;
        while (inputSource.possibleNextKey()) {
            key = cleanedNextKey(inputSource);
            if ("NLQ".contains(String.valueOf(key))) {
                break;
            }
        }
        switch (key) {
            case 'N':
                gameHistory = new StringBuilder();
                startNewGame(inputSource);
                playGame(inputSource, true);
                break;
            case 'L':
                String lastGame = loadGame(inputSource);
                InputSource lastInput = new StringInputSource(lastGame);
                gameHistory = new StringBuilder();
                startNewGame(lastInput);
                playGame(lastInput, false);
                playGame(inputSource, true);
                break;
            case 'Q':
                quitGame();
                break;
        }
    }

    /**
     * If interacting with keyboard, opens a main menu window showing three options.
     */
    private void mainMenuWindow() {
        if (!keyboard) {
            return;
        }
        StdDraw.setCanvasSize(MENU_WIDTH * 16, MENU_WIDTH * 16);
        StdDraw.setXscale(0, MENU_WIDTH);
        StdDraw.setYscale(0, MENU_WIDTH);
        StdDraw.clear(Color.BLACK);

        Font title = new Font("Monoco", Font.BOLD, 25);
        StdDraw.setFont(title);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((float) MENU_WIDTH / 2, 19, "CS61B: The Game");

        Font option = new Font("Monoco", Font.PLAIN, 20);
        StdDraw.setFont(option);
        StdDraw.text((float) MENU_WIDTH / 2, 13, "New Game (N)");
        StdDraw.text((float) MENU_WIDTH / 2, 11, "Load Game (L)");
        StdDraw.text((float) MENU_WIDTH / 2, 9, "Quit (Q)");

        StdDraw.show();
    }

    /**
     * Generates a new Game instance using the input seed and displays the world onscreen.
     */
    private void startNewGame(InputSource inputSource) {
        long seed = processSeed(inputSource);

        game = new Game(WIDTH, HEIGHT, seed);
        gameHistory.append(seed).append('S');

        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(game.WORLD);
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
     */
    private long processSeed(InputSource inputSource) {
        enterSeedWindow("");
        StringBuilder sb = new StringBuilder();
        while (inputSource.possibleNextKey()) {
            char key = enterSeed(inputSource);
            if (Character.isDigit(key)) {
                sb.append(key);
                enterSeedWindow(sb.toString());
            } else if (key == 'S') {
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
     * If interacting with keyboard, opens a window that prompts the user to enter a seed and then
     * displays their entry.
     */
    public void enterSeedWindow(String seed) {
        if (!keyboard) {
            return;
        }
        StdDraw.clear(Color.BLACK);
        Font prompt = new Font("Monoco", Font.PLAIN, 20);
        StdDraw.setFont(prompt);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((float) MENU_WIDTH / 2, 11.5, "Enter a seed and press S");
        StdDraw.text((float) MENU_WIDTH / 2, 9, seed);
        StdDraw.show();
    }

    /**
     * Repeats playing game and updating TERender as the avatar moves around - until the next input
     * key asks to quit or quit-save, which is being monitored in cleanedNextKey() method.
     */
    private void playGame(InputSource inputSource, boolean gameOn) {
        while (gameOn || inputSource.possibleNextKey()) {
            if (inputSource.possibleNextKey()) {
                char key = cleanedNextKey(inputSource);
                game.play(key);
                gameHistory.append(key);
                ter.renderFrame(game.WORLD);
            }
        }
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
     * Loads gameHistory from file. If file doesn't exist, creates a new Game instance.
     * FIXME: no need to play the whole thing through !!!
     */
    private String loadGame(InputSource inputSource) {
        File file = new File("./save_data.txt");
        if (file.exists()) {
            try {
                FileInputStream fos = new FileInputStream(file);
                ObjectInputStream oos = new ObjectInputStream(fos);
                return (String) oos.readObject();
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
        }

        return null;
        // In the case no Game has been saved yet, starts a new one.
                             // FIXME pop up seed entry window?
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

}
