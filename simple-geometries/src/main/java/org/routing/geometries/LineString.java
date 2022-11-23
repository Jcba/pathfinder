package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "type",
        "coordinates"
})
public record LineString(@JsonIgnore List<Point> points) implements Geometry {

    @JsonGetter("type")
    public String getType() {
        return "LineString";
    }

    @JsonGetter("coordinates")
    public List<List<Float>> getCoordinates() {
        return points.stream().map(Point::getCoordinates).toList();
    }
}
