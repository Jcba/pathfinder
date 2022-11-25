package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "type",
        "coordinates"
})
public class LineString extends AbstractGeometry<List<List<Double>>> {
    @JsonIgnore
    private final List<Point> points;

    public LineString(List<Point> points) {
        this.points = points;
    }

    public String getType() {
        return "LineString";
    }

    public List<List<Double>> getCoordinates() {
        return points.stream().map(Point::getCoordinates).toList();
    }

    public List<Point> getPoints() {
        return points;
    }
}
