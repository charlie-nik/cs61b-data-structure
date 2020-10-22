import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * BnBSolver for the Bears and Beds problem. Each Bear can only be compared to Bed objects and each Bed
 * can only be compared to Bear objects. There is a one-to-one mapping between Bears and Beds, i.e.
 * each Bear has a unique size and has exactly one corresponding Bed with the same size.
 * Given a list of Bears and a list of Beds, create lists of the same Bears and Beds where the ith Bear is the same
 * size as the ith Bed.
 */
public class BnBSolver {

    private List<Bear> sortedBears;
    private List<Bed> sortedBeds;
    private Node root;

    public BnBSolver(List<Bear> bears, List<Bed> beds) {
        // TODO: Fix me.
        root = new Node(beds.get(0));
        buildLLRB(root, beds, bears.get(0), 1);
        sortedBears = new ArrayList<>();
        sortedBeds = new ArrayList<>();
    }

    /**
     * Returns List of Bears such that the ith Bear is the same size as the ith Bed of solvedBeds().
     */
    public List<Bear> solvedBears() {
        // TODO: Fix me.
        return sortedBears;
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        // TODO: Fix me.
        return sortedBeds;
    }

    /**
     * Use one random bear as the comparator to build a LLRB of beds.
     */
    private void buildLLRB(Node curr, List<Bed> beds, Bear bearCeleb, int index) {
        if (index == beds.size()) {
            return;
        }
        if (curr null) {

        }
        int zero = curr.bed.compareTo(bearCeleb);
        int val = beds.get(index).compareTo(bearCeleb);
        if (val >= zero) {
            buildLLRB(curr.right, beds, bearCeleb, index);
        } else {
            buildLLRB(curr.left, beds, bearCeleb, index);
        }
        int center = beds.get(0).compareTo(bear);
        for (int i = 1; i < beds.size(); i++) {
            if (beds.get(i).compareTo(bear) > center) {
                center.
            }
        }
    }

    private void rotateLeft() {

    }

    private void rotateRight() {

    }

    private class Node {
        private Bed bed;
        private Node left;
        private Node right;
        Node(Bed bed) {
            this.bed = bed;
        }
    }
}
