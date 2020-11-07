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
        StdDraw.text((float) Engine.MENU_WIDTH / 2, 13, "New Game (N)");
        StdDraw.text((float) Engine.MENU_WIDTH / 2, 11, "Load Game (L)");
        StdDraw.text((float) Engine.MENU_WIDTH / 2, 9, "Quit (Q)");

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
        Font prompt = new Font("Monoco", Font.PLAIN, 20);
        StdDraw.setFont(prompt);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((float) Engine.MENU_WIDTH / 2, (float) Engine.MENU_HEIGHT / 3 + 1.5,
                "Enter a seed and press 'S'");
        StdDraw.text((float) Engine.MENU_WIDTH / 2, (float) Engine.MENU_HEIGHT / 3 - 1,
                "[" + seed + "]");
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

    protected static void levelWindow(int level, boolean keyboard) {
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
        levelWindowHelper(level, "");
        levelWindowHelper(level, levelMessage + "");
        levelWindowHelper(level, levelMessage + " .");
        levelWindowHelper(level, levelMessage + " . .");
        levelWindowHelper(level, levelMessage + " . . .");
        levelWindowHelper(level, levelMessage + " .");
        levelWindowHelper(level, levelMessage + " . .");
        levelWindowHelper(level, levelMessage + " . . .");
    }

    private static void levelWindowHelper(int level, String levelMessage) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monoco", Font.BOLD, 32));
        StdDraw.textLeft((float) Engine.WIDTH / 4, (float) Engine.HEIGHT / 2 + 1,
                "Level " + level);
        StdDraw.setFont(new Font("Monoco", Font.PLAIN, 18));
        StdDraw.textLeft((float) Engine.WIDTH / 4, (float) Engine.HEIGHT / 2 - 1.5,
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
            levelResultHelper("You've brought the world back to life.");
            StdDraw.pause(800);
            System.exit(0);
        } else if (result == 1) {
            levelResultHelper("You've passed!");
        } else {
            levelResultHelper("Ugh! There's no more you.");
            StdDraw.pause(800);
            System.exit(0);
        }
    }

    private static void levelResultHelper(String firstLine) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monoco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text((float) Engine.WIDTH / 2, (float) Engine.HEIGHT / 2, firstLine);
        StdDraw.show();
        StdDraw.pause(1800);
    }

    /**
     * Displays a Head Up Display that includes a description of the moused-over tile and a
     * count of the number of wild flowers yet to be plucked.
     */
    protected static void headUpDisplay(TETile[][] world, int flowers, boolean keyboard) {
        if (!keyboard) {
            return;
        }
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x < 0 || x >= Engine.WIDTH || y < 0 || y >= Engine.HEIGHT) {
            return;
        }
        TETile tile = world[x][y];

        if (tile == Tileset.NOTHING) {
            writeHUD(flowers, "nothing - unknown, unlimited, free");
        } else if (tile == Tileset.FLOOR) {
            writeHUD(flowers, "floor - harmless, taciturn, reliable");
        } else if (tile == Tileset.AVATAR) {
            writeHUD(flowers, "you - homeless, nameless, clueless");
        } else if (tile == Tileset.FLOWER) {
            writeHUD(flowers, "flower - pick, pluck, collect");
        } else if (tile == Tileset.LOCKED_DOOR) {
            writeHUD(flowers, "locked door - desirable, inviting, unreachable");
        } else if (tile == Tileset.UNLOCKED_DOOR) {
            writeHUD(flowers, "unlocked door - tamed, available, penetrable");
        } else if (tile == Tileset.TELEPORT_ENTRY) {
            writeHUD(flowers, "teleport entry - futuristic, impossible, dumbfounding");
        } else if (tile == Tileset.TELEPORT_EXIT) {
            writeHUD(flowers, "teleport exit - reborn, dazed, transformed");
        } else if (tile == Tileset.ENEMY) {
            writeHUD(flowers, "hostile NPC - ill-willed, ravenous, mechanical");
        } else {
            writeHUD(flowers, "wall - cold, cruel, firm");
        }
    }

    /**
     * Displays tile description on the upper left corner and the number of remaining wild
     * flowers in the lower left corner.
     */
    private static void writeHUD(int flowers, String s) {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monoco", Font.PLAIN, 15));
        StdDraw.textLeft(1, 28.5, s);
        StdDraw.textLeft(1, 1.5, "wild flowers: " + flowers + " / 3");
        StdDraw.show();
    }

    /**
     * For testing.
     */
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        levelResultWindow(3, -1, true);
    }

}
