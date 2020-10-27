package byow.Graph;

import byow.Core.SpaceUtils.*;

public class Edge {
    private final Position from, to;
    private final int weight;

    public Edge(Position from, Position to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    public Position from() {
        return from;
    }
    public Position to() {
        return to;
    }
    public int weight() {
        return weight;
    }
}
