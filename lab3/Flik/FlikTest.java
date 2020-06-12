import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {

    @Test
    public void testFlik() {

        assertFalse(Flik.isSameNumber(400, 499));
        assertFalse(Flik.isSameNumber(11, 13));
        assertTrue(Flik.isSameNumber(11, 11));

    }
}

