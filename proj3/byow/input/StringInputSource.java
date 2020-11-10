package byow.input;

/**
 * Based on @author Josh Hug's code.
 */
public class StringInputSource implements InputSource {
    private final String input;
    private int index;

    public StringInputSource(String input) {
        this.input = input;
        index = 0;
    }

    @Override
    public char getNextKey() {
        return input.charAt(index++);
    }

    @Override
    public boolean possibleNextKey() {
        return index < input.length();
    }
}
