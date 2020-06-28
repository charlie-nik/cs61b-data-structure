import org.junit.Test;
import static org.junit.Assert.*;

public class RollingStringTest {

    @Test
    public void sanityGenericTest() {
        try {
            RollingString a = new RollingString("hello", 5);
            RollingString b = new RollingString("yesterday", 9);
            RollingString c = new RollingString("-_-", 3);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddChar() {
        RollingString a = new RollingString("hello", 5);
        a.addChar('!');
        assertEquals("ello!", a.toString());
        a.addChar('o');
        a.addChar('l');
        assertEquals("lo!ol", a.toString());
    }

    @Test
    public void testEquals() {
        RollingString a = new RollingString("hello", 5);
        RollingString a1 = new RollingString("olleh", 5);
        RollingString a2 = new RollingString("he  m", 5);
        assertNotEquals(a, a1);
        assertNotEquals(a, a2);
        RollingString b = new RollingString("hello", 5);
        assertEquals(a, b);
    }
}
