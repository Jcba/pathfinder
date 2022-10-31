package org.routing.model;

import org.routing.geometries.Point;

import java.io.Serializable;

/**
 * @param coordinate not really needed for routing -- remove in feature
 */
public record Node(Point coordinate) implements Serializable {

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

        return coordinate() != null ? coordinate().equals(node.coordinate()) : node.coordinate() == null;
    }

    @Override
    public int hashCode() {
        return coordinate() != null ? coordinate().hashCode() : 0;
    }
}
