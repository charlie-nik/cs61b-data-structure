package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


public class TestSimpleOomage {

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void testHashCodePerfect() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 15);
        SimpleOomage ooB = new SimpleOomage(5, 15, 10);
        assertNotEquals(ooA.hashCode(), ooB.hashCode());

        SimpleOomage ooC = new SimpleOomage(10, 5, 15);
        assertNotEquals(ooA.hashCode(), ooC.hashCode());
        assertNotEquals(ooB.hashCode(), ooC.hashCode());

        SimpleOomage ooD = new SimpleOomage(10, 15, 5);
        assertNotEquals(ooA.hashCode(), ooD.hashCode());
        assertNotEquals(ooB.hashCode(), ooD.hashCode());
        assertNotEquals(ooC.hashCode(), ooD.hashCode());

        SimpleOomage ooE = new SimpleOomage(15, 5, 10);
        assertNotEquals(ooA.hashCode(), ooE.hashCode());
        assertNotEquals(ooB.hashCode(), ooE.hashCode());
        assertNotEquals(ooC.hashCode(), ooE.hashCode());
        assertNotEquals(ooD.hashCode(), ooE.hashCode());

        SimpleOomage ooF = new SimpleOomage(15, 10, 5);
        assertNotEquals(ooA.hashCode(), ooF.hashCode());
        assertNotEquals(ooB.hashCode(), ooF.hashCode());
        assertNotEquals(ooC.hashCode(), ooF.hashCode());
        assertNotEquals(ooD.hashCode(), ooF.hashCode());
        assertNotEquals(ooE.hashCode(), ooF.hashCode());

    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }

    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }

    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
