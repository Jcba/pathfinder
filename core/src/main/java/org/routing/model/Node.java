package org.routing.model;

import org.routing.geometries.Point;

import java.io.Serializable;

public class Node implements Serializable {

    public Node(Point coordinate) {
        this.coordinate = coordinate;
    }

    private Point coordinate;

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
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
        if (getCoordinate() == null) {
            return 0;
        }
        double lat = getCoordinate().getLat();
        double lon = getCoordinate().getLon();

        return (((int) (lat * 6)) << 8) + ((int) lon * 6);
    }
}
