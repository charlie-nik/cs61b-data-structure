package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

/**
 * Primary class for user interface. Including a collection of various windows that open up under
 * different circumstances, and a constant Head Up Display showing essential game information.
 * Everything will be bypassed, however, if interacting with string rather than keyboard.
 */
public class UI {

    /**
     * Opens a main menu window displaying three options.
     */
    protected static void mainMenuWindow(boolean keyboard) {
        if (!keyboard) {
            return;
        }
        StdDraw.setCanvasSize(Engine.MENU_WIDTH * 16, Engine.MENU_HEIGHT * 16);
        StdDraw.setXscale(0, Engine.MENU_WIDTH);
        StdDraw.setYscale(0, Engine.MENU_HEIGHT);
        StdDraw.clear(Color.BLACK);

        Font title = new Font("Monoco", Font.BOLD, 25);
        StdDraw.setFont(title);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((float) Engine.MENU_WIDTH / 2, 19, "CS61B: The Game");

        Font option = new Font("Monoco", Font.PLAIN, 20);
        StdDraw.setFont(option);
        StdDraw.text((float) Engine.MENU_WIDTH / 2, 12, "New Game (N)");
        StdDraw.text((float) Engine.MENU_WIDTH / 2, 10, "Load Game (L)");
        StdDraw.text((float) Engine.MENU_WIDTH / 2, 8, "Quit (Q)");

        StdDraw.show();
    }

    /**
     * When user chooses "Load Game" and yet no previous game has been saved, displays a load
     * failure window that informs the user of the failure and returns to the main menu.
     */
    protected static void loadFailureWindow(boolean keyboard) {
        if (!keyboard) {
            return;
        }
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monoco", Font.PLAIN, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((float) Engine.MENU_WIDTH / 2, (float) Engine.MENU_HEIGHT / 2 + 1,
                "No previous game has been saved.");
        StdDraw.text((float) Engine.MENU_WIDTH / 2, (float) Engine.MENU_HEIGHT / 2 - 1,
                "Please start a new game.");
        StdDraw.show();
        StdDraw.pause(4000);
    }

    /**
     * Opens a window that prompts the user to enter a seed and then displays their entry.
     */
    protected static void enterSeedWindow(String seed, boolean keyboard) {
        if (!keyboard) {
            return;
        }
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monoco", Font.PLAIN, 20));
        StdDraw.text((float) Engine.MENU_WIDTH / 2, (float) Engine.MENU_HEIGHT / 3 + 1.5,
                "Enter a seed and press (S)");
        StdDraw.setFont(new Font("Monoco", Font.PLAIN, 18));
        StdDraw.text((float) Engine.MENU_WIDTH / 2, (float) Engine.MENU_HEIGHT / 3 - 1.5, seed);
        StdDraw.show();
    }

    /**
     * If world generation using the provided seed exceeds time limit, opens a window displaying
     * error message.
     */
    protected static void worldGenerationFailureWindow() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monoco", Font.PLAIN, 18);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft((float) Engine.WIDTH / 4, (float) Engine.HEIGHT / 2 + 1,
                "Time limit exceeded.");
        StdDraw.textLeft((float) Engine.WIDTH / 4, (float) Engine.HEIGHT / 2 - 1,
                "Failed to generate a new world using the current seed.");
        StdDraw.textLeft((float) Engine.WIDTH / 4, (float) Engine.HEIGHT / 2 - 3,
                "Please wait . . .");
        StdDraw.show();
        StdDraw.pause(4500);
    }

    protected static void levelTitleWindow(int level, boolean keyboard) {
        if (!keyboard) {
            return;
        }
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
        StdDraw.pause(1700);

        String levelMessage = null;
        switch (level) {
            case 1 -> levelMessage = "Collect 3 flowers to unlock the door";
            case 2 -> levelMessage = "Future transportation technology is installed. Give it a try";
            case 3 -> levelMessage = "You've got company. It's slow, but it's coming for you";
            case 4 -> levelMessage = "It has underestimated you! It'll move faster, so should you";
            case 5 -> levelMessage = "It calls for backup. No more fair game, it's 1 VS 3 now";
            case 6 -> levelMessage = "Now you've antagonized the whole colony. "
                                   + "They're closing in fast";
            case 7 -> levelMessage = "Ultimatum! Beware of the shrinking vision. "
                                   + "Darkness before dawn";
        }
        levelTitleHelper(level, "");
        levelTitleHelper(level, levelMessage + "");
        levelTitleHelper(level, levelMessage + " .");
        levelTitleHelper(level, levelMessage + " . .");
        levelTitleHelper(level, levelMessage + " . . .");
        levelTitleHelper(level, levelMessage + " .");
        levelTitleHelper(level, levelMessage + " . .");
        levelTitleHelper(level, levelMessage + " . . .");
    }

    private static void levelTitleHelper(int level, String levelMessage) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monoco", Font.BOLD, 32));
        StdDraw.textLeft((float) Engine.WIDTH / 4, (float) Engine.HEIGHT / 2 + 1,
                "Level " + level);
        StdDraw.setFont(new Font("Monoco", Font.PLAIN, 18));
        StdDraw.textLeft((float) Engine.WIDTH / 4, (float) Engine.HEIGHT / 2 - 1.6,
                levelMessage);
        StdDraw.show();
        if (levelMessage.isEmpty()) {
            StdDraw.pause(400);
        }
        StdDraw.pause(700);
    }

    /**
     * When the game is won, displays a "You've won!" celebration-affirmation thingy. FIXME
     */
    protected static void levelResultWindow(int level, int result, boolean keyboard) {
        if (!keyboard) {
            return;
        }
        if (level == 7 && result == 1) {
            levelResultHelper("You've brought the world back to life.", 30, "", 0);
            StdDraw.pause(800);
            System.exit(0);
        } else if (result == 1) {
            levelResultHelper("You've passed!", 30, "", 0);
        } else {
            levelResultHelper("!", 80, "", 0);
            StdDraw.pause(300);
            levelResultHelper("!", 80, "There's no more you . . .", 20);
            StdDraw.pause(800);
            System.exit(0);
        }
    }

    private static void levelResultHelper(String line1, int size1, String line2, int size2) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monoco", Font.BOLD, size1));
        StdDraw.text((float) Engine.WIDTH / 2, (float) Engine.HEIGHT / 2, line1);
        StdDraw.setFont(new Font("Monoco", Font.BOLD, size2));
        StdDraw.text((float) Engine.WIDTH / 2, (float) Engine.HEIGHT / 2 - 4.5, line2);
        StdDraw.show();
        StdDraw.pause(1800);
    }

    /**
     * Opens a window displaying control keys and their corresponding actions.
     */
    protected static void controlWindow(int level, boolean keyboard) {
        if (!keyboard) {
            return;
        }
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.setFont(new Font("Monoco", Font.BOLD, 27));
        StdDraw.text((float) Engine.WIDTH / 2, 23.5, "(W)");
        StdDraw.text((float) Engine.WIDTH / 2, 20, "(A)        (D)");
        StdDraw.text((float) Engine.WIDTH / 2, 16.5, "(S)");

        StdDraw.setFont(new Font("Monoco", Font.PLAIN, 16));
        StdDraw.text((float) Engine.WIDTH / 2, 7, "Continue (C)");
        StdDraw.text((float) Engine.WIDTH / 2, 5.2, "Back to Menu (M)");
        StdDraw.text((float) Engine.WIDTH / 2, 3.4, "Save and Quit (:Q)        Quit (Q)");
        if (level >= 2) {
            StdDraw.text((float) Engine.WIDTH / 2, 12, "De-activate      Re-activate Teleport (T)");
        }
        if (level >= 3) {
            StdDraw.text((float) Engine.WIDTH / 2, 10.2, "Show      Hide NPC's Path (P)");
        }
        StdDraw.setFont(new Font("Monoco", Font.BOLD, 19));
        StdDraw.text((float) Engine.WIDTH / 2 + 2.5, 3.4, ".");
        if (level >= 2) {
            StdDraw.text((float) Engine.WIDTH / 2 - 2.88, 12, "/");
        }
        if (level >= 3) {
            StdDraw.text((float) Engine.WIDTH / 2 - 3.2, 10.2, "/");
        }

        StdDraw.show();
    }

    /**
     * Displays a Head Up Display that includes a description of the moused-over tile and a
     * count of the number of wild flowers yet to be plucked.
     */
    protected static void headUpDisplay(Game game, boolean keyboard) {
        if (!keyboard) {
            return;
        }
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x < 0 || x >= Engine.WIDTH || y < 0 || y >= Engine.HEIGHT) {
            return;
        }
        TETile tile = game.surfaceWorld[x][y];

        if (tile.equals(Tileset.NOTHING)) {
            writeHUD(game, "nothing - unknown, unlimited, free");
        } else if (tile.equals(Tileset.WALL)) {
            writeHUD(game, "wall - cold, cruel, firm");
        } else if (tile.equals(Tileset.FLOOR)) {
            writeHUD(game, "floor - harmless, taciturn, reliable");
        } else if (tile.equals(Tileset.AVATAR)) {
            writeHUD(game, "you - homeless, nameless, clueless");
        } else if (tile.equals(Tileset.FLOWER)) {
            writeHUD(game, "flower - pick, pluck, collect");
        } else if (tile.equals(Tileset.LOCKED_DOOR)) {
            writeHUD(game, "locked door - desirable, inviting, unreachable");
        } else if (tile.equals(Tileset.UNLOCKED_DOOR)) {
            writeHUD(game, "unlocked door - tamed, available, penetrable");
        } else if (tile.equals(Tileset.TELEPORT_ENTRY)) {
            writeHUD(game, "teleport entry - futuristic, impossible, dumbfounding");
        } else if (tile.equals(Tileset.TELEPORT_EXIT)) {
            writeHUD(game, "teleport exit - reborn, dazed, transformed");
        } else if (tile.equals(Tileset.ENEMY)) {
            writeHUD(game, "hostile NPC - ill-willed, ravenous, mechanical");
        }
    }

    /**
     * Displays tile description on the upper left corner. Displays the current level and the number
     * of remaining wild flowers in the lower left corner. Displays a general description in the
     * upper middle.
     */
    private static void writeHUD(Game game, String description) {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monoco", Font.BOLD, 16));
        StdDraw.textLeft(1.5, 1.5, "LEVEL " + game.level);
        StdDraw.setFont(new Font("Monoco", Font.PLAIN, 16));
        StdDraw.textLeft(6.7, 1.5, "wild flowers: " + game.remainingFlowers + " " + "/ 3");
        StdDraw.textLeft(1.5, 28.5, description);
        StdDraw.textRight(Engine.WIDTH - 2, 28.5, "Controls (C)");
        switch (game.level) {
            case 1 -> StdDraw.text((float) Engine.WIDTH / 2, 28.5,
                    "Mouse over to read object's description.");
            case 2 -> StdDraw.text((float) Engine.WIDTH / 2, 28.5,
                    "Press (T) to de-activate teleport. Press again to re-activate.");
            case 3 -> StdDraw.text((float) Engine.WIDTH / 2, 28.5,
                    "Press (P) to show NPC's projected path. Press again to hide.");
        }
        StdDraw.show();
    }

    /**
     * For testing.
     */
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        controlWindow(3, true);
    }

}
