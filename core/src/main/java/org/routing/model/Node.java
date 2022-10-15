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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return getCoordinate() != null ? getCoordinate().equals(node.getCoordinate()) : node.getCoordinate() == null;
    }

    @Override
    public int hashCode() {
        return getCoordinate() != null ? getCoordinate().hashCode() : 0;
    }
}
