package org.routing.model;

import java.io.Serializable;

public class Node implements Serializable {
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
