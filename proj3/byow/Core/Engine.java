package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private InputSource inputSource;
    private Game game;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        inputSource = new StringInputDevice(input);

        selectMainMenu();

        return game.WORLD;
    }

    /**
     * According to input key, either starts a new game, loads a previous game, or quits. Ignores
     * input keys that don't make sense.
     */
    private void selectMainMenu() {
        char key = cleanedNextKey();
        while (!"NLQ".contains(String.valueOf(key)) && inputSource.possibleNextInput()) {
            key = cleanedNextKey();
        }
        switch (key) {
            case 'N':
                startNewGame();
                playGame();
            case 'L':
                loadGame();
                playGame();
            case 'Q':
                quitGame();
        }
    }

    /**
     * Generates a new Game instance.
     */
    private void startNewGame() {
        long seed = processSeed();
        game = new Game(WIDTH, HEIGHT, seed);
    }

    /**
     * Processes input keys to get the seed. Makes sure seed value doesn't exceed Java's maximum
     * integer value.
     */
    private long processSeed() {
        StringBuilder sb = new StringBuilder();
        while (inputSource.possibleNextInput()) {
            char key = cleanedNextKey();
            if (Character.isDigit(key)) {
                sb.append(key);
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
     * Repeats playing game and updating TERender as the avatar moves around until the next input
     * key asks to quit or quit-save.
     */
    private void playGame() {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(game.WORLD);

        while (true) {
            if (inputSource.possibleNextInput()) {
                char key = cleanedNextKey();
                game.play(key);
                ter.renderFrame(game.WORLD);
            }
        }
    }

    /**
     * Saves Game instance to file.
     */
    private void saveGame() {
        File file = new File("./save_data.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(game);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Loads Game instance from file. If file doesn't exist, creates a new Game instance.
     */
    private void loadGame() {
        File file = new File("./save_data.txt");
        if (file.exists()) {
            try {
                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream os = new ObjectInputStream(fs);
                game = (Game) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        } else {
            startNewGame();  // In the case no Game has been saved yet, starts a new one.
        }
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
    private char cleanedNextKey() {
        char key = Character.toUpperCase(inputSource.getNextKey());
        if (key == ':' && inputSource.possibleNextInput()) {
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

    public void mainMenu() {
        int width = 40, height = 40;
        StdDraw.setCanvasSize(width * 16, height * 16);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        Font title = new Font("Monoco", Font.BOLD, 30);
        StdDraw.setFont(title);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((float) width / 2, (float) height / 2, "CS61B: The Game");
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.ter.renderFrame(engine.interactWithInputString("lddww"));
    }
}
