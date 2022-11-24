package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

import static java.lang.Math.*;

@JsonPropertyOrder({
        "type",
        "coordinates"
})
public record Point (@JsonIgnore double lon, @JsonIgnore double lat) implements Geometry {

    @JsonGetter("type")
    public String getType() {
        return "Point";
    }

    @JsonGetter("coordinates")
    public List<Double> getCoordinates() {
        return List.of(lat, lon);
    }

    @JsonIgnore
    public float distance(Point other) {
        var earthRadius = 6371000.0; //meters
        var dLat = Math.toRadians((other.lat - this.lat));
        var dLng = Math.toRadians((other.lon - this.lon));
        var a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(this.lat)) *
                        cos(Math.toRadians(other.lat)) *
                        sin(dLng / 2) * sin(dLng / 2);
        var c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return (float) (earthRadius * c);
    }
}
