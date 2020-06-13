/**
 * @source: StudentArrayDequeLauncher.java
 */

import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> exp1 = new ArrayDequeSolution<>();

        for (int i = 0; i < 10; i++) {
            double randomNumber = StdRandom.uniform();

            //if (randomNumber < 0.2) {
                //assertEquals("isEmpty()", true, sad1.isEmpty());
            if (randomNumber < 0.4) {
                sad1.addFirst(i);
                exp1.addFirst(i);
                assertEquals("addFirst(" + i + ")", exp1.size(), sad1.size());
            } else if (randomNumber < 0.6) {
                sad1.addLast(i);
                exp1.addLast(i);
                Integer a = sad1.removeFirst();
                Integer b = exp1.removeFirst();
                assertEquals("addLast(" + i + ")\nremoveFirst()", b, a);
            } else if (randomNumber < 0.8) {
                sad1.addFirst(i);
                exp1.addFirst(i);
                Integer a = sad1.removeLast();
                Integer b = exp1.removeLast();
                assertEquals("addFirst(" + i + ")\nremoveLast()", b, a);
            } else {
                sad1.addFirst(i);
                exp1.addFirst(i);
                sad1.addLast(i * 10);
                exp1.addLast(i * 10);
                Integer a = sad1.get(1);
                Integer b = exp1.get(1);
                assertEquals("addFirst(" + i + ")\naddLast(" + (i+1) + ")\nget(1)", b, a);
            }
        }
    }
}
