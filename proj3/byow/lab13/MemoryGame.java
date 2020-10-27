package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Random;

public class MemoryGame {
    private final int width;
    private final int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        /*if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);*/
        // Not sure how to use skeleton codes concerning seed entry... commented out.

        MemoryGame game = new MemoryGame(40, 40);
        game.startGame();
    }

    public MemoryGame(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random();
    }

    /**
     * Generates random string of letters of length N.
     */
    public String generateRandomString(int n) {
        int numOfOptions = CHARACTERS.length;
        double[] probabilities = new double[numOfOptions];
        Arrays.fill(probabilities, 1.0 / numOfOptions);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int index = RandomUtils.discrete(rand, probabilities);
            char c = CHARACTERS[index];
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Takes the string and displays it in the center of the screen.
     * If game is not over, displays relevant game information at the top of the screen.
     */
    public void drawFrame(String s) {
        StdDraw.clear();
        Font font = new Font("Monoco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(width / 2.0, height / 2.0, s);

        if (!gameOver) {
            Font header = new Font("Monoco", Font.PLAIN, 18);
            StdDraw.setFont(header);
            StdDraw.textLeft(0.8, height - 1.2, "Round: " + round);
            if (playerTurn) {
                StdDraw.text(width / 2.0, height - 1.2, "Type!");
            } else {
                StdDraw.text(width / 2.0, height - 1.2, "Watch!");
            }
            StdDraw.line(0, height - 2, width, height - 2.1);
        }

        StdDraw.show();
    }

    /**
     * Displays each character in letters, making sure to blank the screen between letters.
     */
    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            char c = letters.charAt(i);
            drawFrame(Character.toString(c));
            StdDraw.pause(1000);        // display each letter for 1 second
            if (i < letters.length() - 1) {
                drawFrame("");
                StdDraw.pause(500);     // 0.5 second break between characters
            } else {
                playerTurn = true;         // begin player's turn immediately after last letter
                drawFrame("");
            }
        }
    }

    /**
     * Reads N letters of player input.
     */
    public String solicitNCharsInput(int n) {
        StringBuilder sb = new StringBuilder();
        while (sb.toString().length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                sb.append(StdDraw.nextKeyTyped());
                drawFrame(sb.toString());   // display what player has typed so far
            }
        }
        return sb.toString();
    }

    public void startGame() {
        round = 1;
        gameOver = false;

        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);
            StdDraw.pause(2000);

            String prompt = generateRandomString(round);
            flashSequence(prompt);
            String answer = solicitNCharsInput(round);
            drawFrame(answer);
            StdDraw.pause(500);     // let player's full answer stay on-screen for 0.5 second

            if (!answer.equals(prompt)) {
                gameOver = true;
                drawFrame("Game over! You made it to round: " + round);
            } else {
                round++;
            }
        }
    }

}
