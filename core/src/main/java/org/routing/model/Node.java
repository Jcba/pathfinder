package org.routing.model;

import org.routing.libgeo.geojson.Point;
import org.routing.storage.KeyProvider;

import java.io.Serializable;

public class Node implements Serializable, KeyProvider {

    private final long id;

    public Node(long id, Point coordinate) {
        this.id = id;
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
    public String getType() {
        return "node";
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return getId() == node.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
