package model;

public class Node {
    private final Point coordinate;

    public Node(Point coordinate) {
        this.coordinate = coordinate;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return "{" +
                "point: " + coordinate +
                '}';
    }
}
