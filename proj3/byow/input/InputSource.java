package byow.input;

/**
 * Based on @author Josh Hug's code.
 */
public interface InputSource {
    boolean possibleNextKey();
    char getNextKey();
}
