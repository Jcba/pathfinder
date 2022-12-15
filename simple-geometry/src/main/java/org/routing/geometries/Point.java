package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.lang.Math.*;

public class Point extends AbstractGeometry<Double[]> {

    private final double lon;
    private final double lat;

    public Point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @JsonGetter("type")
    public String getType() {
        return "Point";
    }

    @JsonGetter("coordinates")
    public Double[] getCoordinates() {
        return new Double[]{lon, lat};
    }

    @JsonIgnore
    public double getLon() {
        return lon;
    }

    @JsonIgnore
    public double getLat() {
        return lat;
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
