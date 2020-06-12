/** source: LinkedListDequeTest.java */

import org.junit.Test;
import org.junit.Assert.*;

public class ArrayDequeTest {

    // Check size
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkItem(int index, int expected, int actual) {
        if (expected != actual) {
            System.out.println("get(" + index + ") returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(String test, boolean passed) {
        if (passed) {
            System.out.println("Test " + test + " passed!");
        } else {
            System.out.println("Test " + test + " failed!");
        }
    }

    /** Adds an item to the front and to the back,
     * makes sure the order and the resizing are correct. */
    public static void addTest() {

        System.out.println("########## Running add() test ##########");

        /** Test 01: construct an empty list. */
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // size = 0, nextFirst = 0, nextLast = 1
        boolean passed1 = checkSize(0, ad1.size());

        printTestStatus("Construct()", passed1);


        /** Test 02: addFirst() */
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        // ad1 = (1, 2, 3)
        boolean passed2 = checkItem(0, 1, ad1.get(0));
        passed2 = checkItem(1, 2, ad1.get(1)) && passed2;
        passed2 = checkItem(2, 3, ad1.get(2)) && passed2;
        // size = 3
        passed2 = checkSize(3, ad1.size()) && passed2;

        printTestStatus("addFirst()", passed2);


        /** Test 03: addLast() */
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.addLast(9);
        ad1.addLast(10);
        // ad1 = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        boolean passed3 = checkItem(3, 4, ad1.get(3));
        passed3 = checkItem(5, 6, ad1.get(5)) && passed3;
        passed3 = checkItem(7, 8, ad1.get(7)) && passed3;
        passed3 = checkItem(9, 10, ad1.get(9)) && passed3;
        // size = 10
        passed3 = checkSize(10, ad1.size()) && passed3;

        printTestStatus("addLast()", passed2);
        System.out.println();

    }

    /** Removes an item from the front and from the back,
     * makes sure the order and the resizing are correct. */
    public static boolean removeTest(ArrayDeque ad2) {

        System.out.println("########## Running remove() test ##########");

        /** Test 04: removeFirst(). */
        ad2.removeFirst();
        // ad2 = (2, 3, 4, 5, 6, 7, 8, 9, 10)
        boolean passed4 = checkItem(0, 2, (Integer) ad2.get(0));
        passed4 = checkItem(4, 6, (Integer) ad2.get(4)) && passed4;
        passed4 = checkItem(7, 9, (Integer) ad2.get(7)) && passed4;
        // size = 9
        passed4 = checkSize(9, ad2.size()) && passed4;

        printTestStatus("removeFirst()", passed4);


        /** Test 05: addLast() */

        ad2.removeLast();
        ad2.removeLast();
        ad2.removeLast();
        ad2.removeFirst();
        // ad2 = (3, 4, 5, 6, 7)
        boolean passed5 = checkItem(0, 3, (Integer) ad2.get(0));
        passed5 = checkItem(3, 6, (Integer) ad2.get(3)) && passed5;
        passed5 = checkItem(4, 7, (Integer) ad2.get(4)) && passed5;
        // size = 5
        passed5 = checkSize(5, ad2.size()) && passed5;

        printTestStatus("removeLast()", passed5);
        System.out.println();

        if (passed4 && passed5) {
            return true;
        } else {
            return false;
        }

    }

    public static void deepCopyTest(ArrayDeque other) {

        System.out.println("########## Running deepCopy() test ##########");

        // I have to change <items> to "public" in order to run this test. I've changed it back now.
        ArrayDeque ad3 = new ArrayDeque<>(other);
        boolean passed6 = true;
        for (int i = 0; i < ad3.items.length; i++) {
            passed6 = ad3.get(i) == other.get(i) && passed6;
        }
        printTestStatus("deepCopy()", passed6);

    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");

        addTest();

        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        ad2.addFirst(3);
        ad2.addFirst(2);
        ad2.addFirst(1);
        ad2.addLast(4);
        ad2.addLast(5);
        ad2.addLast(6);
        ad2.addLast(7);
        ad2.addLast(8);
        ad2.addLast(9);
        ad2.addLast(10);

        removeTest(ad2);
        // ad2 = (3, 4, 5, 6, 7)
        deepCopyTest(ad2);
    }
}
