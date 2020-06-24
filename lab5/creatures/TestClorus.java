package creatures;

import huglife.*;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus c = new Clorus(0.2);
        assertEquals("clorus", c.name());
        assertEquals(0.20, c.energy(), 0.01);
        c.move();
        assertEquals(0.17, c.energy(), 0.01);
        c.move();
        assertEquals(0.14, c.energy(), 0.01);
        c.stay();
        assertEquals(0.13, c.energy(), 0.01);
        c.stay();
        assertEquals(0.12, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
    }

    @Test
    public void testReplicate() {
        Clorus p1 = new Clorus(0.2);
        Clorus p2 = p1.replicate();
        assertNotEquals(p1, p2);
        assertEquals(0.10, p1.energy(), 0.01);
        assertEquals(0.10, p2.energy(), 0.01);
        p1.move();
        p1.stay();
        Clorus p3 = p1.replicate();
        assertNotEquals(p2, p3);
        assertEquals(0.03, p1.energy(), 0.01);
        assertEquals(0.03, p3.energy(), 0.01);
    }

    @Test
    public void testAttack() {
        Clorus c = new Clorus(2);
        Clorus c1 = new Clorus(0.2);
        c.attack(c1);
        assertEquals(2.2, c.energy(), 0.01);
        Plip p = new Plip(0.5);
        c.attack(p);
        assertEquals(2.7, c.energy(), 0.01);
    }

    @Test
    public void testChoose() {

        //No empty squares, stay.
        Clorus c = new Clorus(2);
        Map<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        //If there're Plips in the neighborhood, attack randomly.
        c = new Clorus(2);
        Map<Direction, Occupant> diverse = new HashMap<>();
        diverse.put(Direction.TOP, new Plip(1));
        diverse.put(Direction.BOTTOM, new Empty());
        diverse.put(Direction.LEFT, new Empty());
        diverse.put(Direction.RIGHT, new Clorus(2));

        actual = c.chooseAction(diverse);
        expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);


        //If energy >= 1, replicate randomly.
        c = new Clorus(2);
        Map<Direction, Occupant> clorusAndEmpty = new HashMap<>();
        clorusAndEmpty.put(Direction.TOP, new Clorus());
        clorusAndEmpty.put(Direction.BOTTOM, new Clorus());
        clorusAndEmpty.put(Direction.LEFT, new Impassible());
        clorusAndEmpty.put(Direction.RIGHT, new Empty());

        actual = c.chooseAction(clorusAndEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.RIGHT);

        assertEquals(expected, actual);


        //Otherwise, move to a random empty square.
        c = new Clorus(0.99);

        actual = c.chooseAction(clorusAndEmpty);
        expected = new Action(Action.ActionType.MOVE, Direction.RIGHT);

        assertEquals(expected, actual);
    }

}
