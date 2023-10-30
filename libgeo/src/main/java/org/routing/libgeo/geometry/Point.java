package org.routing.libgeo.geometry;

import static java.lang.Math.*;

public record Point(double lat, double lon) implements Geometry {

    public float distance(Point other) {
        var earthRadius = 6371000.0; //meters
        var dLat = toRadians((other.lat() - this.lat()));
        var dLng = toRadians((other.lon() - this.lon()));
        var a = sin(dLat / 2) * sin(dLat / 2) +
                cos(toRadians(this.lat())) *
                        cos(toRadians(other.lat())) *
                        sin(dLng / 2) * sin(dLng / 2);
        var c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return (float) (earthRadius * c);
    }

}
