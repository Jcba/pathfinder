package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;

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

    public String getType() {
        return type;
    }

    public Double[][] getCoordinates() {
        return coordinates;
    }

    @JsonIgnore
    public List<Point> getPoints() {
        return null;
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
