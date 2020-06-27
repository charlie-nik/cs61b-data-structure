/**
 * Created by hug.
 */
public class ExperimentHelper {

    /** Returns the internal path length for an optimum binary search tree of
     *  size N. Examples:
     *  N = 1, OIPL: 0
     *  N = 2, OIPL: 1
     *  N = 3, OIPL: 2
     *  N = 4, OIPL: 4
     *  N = 5, OIPL: 6
     *  N = 6, OIPL: 8
     *  N = 7, OIPL: 10
     *  N = 8, OIPL: 13
     */
    public static int optimalIPL(int N) {
        int sum = 0;
        for (int i = 0; N > 0; i++) {
            sum += Math.min(Math.pow(2, i), N) * i;
            N -= Math.pow(2, i);
        }
        return sum;
    }

    /** Returns the average depth for nodes in an optimal BST of
     *  size N.
     *  Examples:
     *  N = 1, OAD: 0
     *  N = 5, OAD: 1.2
     *  N = 8, OAD: 1.625
     */
    public static double optimalAverageDepth(int N) {
        return N == 0 ? 0.0 : (float) optimalIPL(N) / N;
    }

    /* Randomly insert an item into BST. */
    public static void randomInsert(BST<Integer> bst, int limit) {
        int item = RandomGenerator.getRandomInt(limit);
        while (bst.contains(item)) {
            item = RandomGenerator.getRandomInt(limit);
        }
        bst.add(item);
    }
}
