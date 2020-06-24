package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;
import huglife.HugLifeUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.awt.*;

public class Clorus extends Creature {
    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blur color.
     */
    private int b;


    /**
     * creates a Clorus with energy equal to E.
     */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /**
     * creates a Clorus with energy equal to 1.
     */
    public Clorus() {
        this(1);
    }

    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return new Color(r, g, b);
    }

    /**
     * Clorus should lose 0.01 energy when staying.
     */
    public void stay() {
        energy -= 0.01;
        if (energy < 0) {
            energy = 0;
        }
    }

    /**
     * Clorus should lose 0.03 energy when moving.
     */
    public void move() {
        energy -= 0.03;
        if (energy < 0) {
            energy = 0;
        }
    }

    /**
     * When Clorus attacks another creature, it should gain the energy
     * of that creature.
     */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /**
     * When Clorus replicates, it keeps 50% energy
     * and gives 50% energy to its offspring.
     * Returns a baby Clorus.
     */
    public Clorus replicate() {
        energy = energy / 2;
        return new Clorus(energy);
    }

    /** Cloruses take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if any Plips, ATTACK one randomly.
     *  2. Otherwise, if energy >= 1, REPLICATE to a random empty square.
     *  3. Otherwise, MOVE to an empty square.
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {

        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plipNeighbors = new ArrayDeque<>();

        for (Direction dir : neighbors.keySet()) {
            String n = neighbors.get(dir).name();
            if (n.equals("empty")) {
                emptyNeighbors.add(dir);
            }
            if (n.equals("plip")) {
                plipNeighbors.add(dir);
            }
        }

        //No empty squares, stay.
        if (emptyNeighbors.size() == 0) {
            return new Action(Action.ActionType.STAY);
        }

        //If there're Plips in the neighborhood, attack randomly.
        if (plipNeighbors.size() > 0) {
            Direction dir = HugLifeUtils.randomEntry(plipNeighbors);
            return new Action(Action.ActionType.ATTACK, dir);
        }

        //If energy >= 1, replicate randomly.
        if (energy >= 1) {
            Direction dir = HugLifeUtils.randomEntry(emptyNeighbors);
            return new Action(Action.ActionType.REPLICATE, dir);
        }

        //Otherwise, move to a random empty square.
        Direction dir = HugLifeUtils.randomEntry(emptyNeighbors);
        return new Action(Action.ActionType.MOVE, dir);
    }
}
