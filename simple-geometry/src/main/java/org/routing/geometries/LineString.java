package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;

/**
 * LineString class. Jackson output marshals to a GeoJSON LineString.
 */
public class LineString extends AbstractGeometry<Double[][]> {

    private Double[][] coordinates;

    public LineString() {
        // default constructor
        type = "LineString";
    }

    public LineString(List<Point> points) {
        this.coordinates = points.stream().map(Point::getCoordinates).toArray(Double[][]::new);
        type = "LineString";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Double[][] getCoordinates() {
        return coordinates;
    }

    @JsonIgnore
    public List<Point> getPoints() {
        return Arrays.stream(getCoordinates()).map(c -> new Point(c[1], c[0])).toList();
    }

    @JsonIgnore
    public double getDistance() {
        double distance = 0;
        List<Point> points = getPoints();
        if (points.size() < 2) {
            return distance;
        }
        for (int i = 0; i < points.size() - 1; i++) {
            distance += points.get(i).distance(points.get(i + 1));
        }
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineString that = (LineString) o;

        return Arrays.deepEquals(getCoordinates(), that.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getCoordinates());
    }
}
