package org.routing.libgeo.geometry;

import java.util.List;

public record LineString(List<Point> points) implements Geometry {

    public double getLength() {
        double length = 0;
        if (points.size() < 2) {
            return length;
        }
        for (int i = 0; i < points.size() - 1; i++) {
            length += points.get(i).distance(points.get(i + 1));
        }
        return length;
    }
}
