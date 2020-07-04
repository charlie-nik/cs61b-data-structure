package bearmaps.hw4.wordladderpuzzle;

import bearmaps.hw4.AStarGraph;
import bearmaps.hw4.WeightedEdge;
import edu.princeton.cs.introcs.In;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that represents the graph of all english words. Word p has an
 * edge to word q if the edit distance between p and q is 1. For example,
 * there is an edge from "horse" to "hose", and "hose" to "horse". There is
 * no edge from "dog" to "deg", because deg isn't a word (at least according to
 * words10000.txt).
 * Created by hug.
 */
public class WordGraph implements AStarGraph<String> {
    private Set<String> words;
    private static final String WORDFILE = "words10000.txt";

    /**
     * Reads the wordfile specified by the wordfile variable.
     */
    private void readWords() {
        words = new HashSet<>();

        In in = new In(WORDFILE);
        while (!in.isEmpty()) {
            String w = in.readString();
            words.add(w);
        }
    }

    /**
     * Computes the edit distance between a and b. From
     * https://rosettacode.org/wiki/Levenshtein_distance.
     */
    private static int editDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }


    /**
     * Creates a new Word.
     */
    public WordGraph() {
        readWords();
    }

    @Override
    public List<WeightedEdge<String>> neighbors(String s) {
        List<WeightedEdge<String>> neighbs = new ArrayList<>();

        List<String> possibles = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            String left = "";
            String right = "";
            if (i > 0) {
                left = s.substring(0, i);
            }
            if (i < s.length() - 1) {
                right = s.substring(i + 1);
            }

            // delete
            possibles.add(left + right);

            // change
            for (String changeOne : addToFront(right, c)) {
                possibles.add(left + changeOne);
            }

            // add
            for (String addOne : addToFront(c + right, null)) {
                possibles.add(left + addOne);
            }
            if (right.equals("")) {
                for (String addToEnd : addToFront("", null)) {
                    possibles.add(left + c + addToEnd);
                }
            }
        }

        for (String w : possibles) {
            if (words.contains(w)) {
                neighbs.add(new WeightedEdge<>(s, w, 1));
            }
        }
        return neighbs;
    }

    // returns a list of new strings with the character (a..z) added to the front of s
    private List<String> addToFront(String s, Character original) {
        List<String> newWords = new ArrayList<>();
        for (char i = 97; i < 123; i++) {
            if (original == null || i != original) {
                newWords.add(i + s);
            }
        }
        return newWords;
    }

    @Override
    public double estimatedDistanceToGoal(String s, String goal) {
        return editDistance(s, goal);
    }
}
