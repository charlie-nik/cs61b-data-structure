import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    @Test
    public void testOffByN() {

        OffByN offBy5 = new OffByN(5);

        assertTrue(offBy5.equalChars('a','f'));
        assertTrue(offBy5.equalChars('f','a'));
        assertFalse(offBy5.equalChars('f','h'));

        OffByN offByN10 = new OffByN(10);

        assertFalse(offByN10.equalChars('g', 'm'));
        assertFalse(offByN10.equalChars('m', 'g'));

    }

}
