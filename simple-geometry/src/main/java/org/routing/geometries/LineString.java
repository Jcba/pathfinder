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
    private List<Point> points;

    public LineString() {
        // default constructor
        type = "LineString";
    }

    public LineString(List<Point> points) {
        this.points = points;
        type = "LineString";
    }

    public String getType() {
        return type;
    }

    public List<List<Double>> getCoordinates() {
        return points.stream().map(Point::getCoordinates).toList();
    }

    public List<Point> getPoints() {
        return points;
    }
}
