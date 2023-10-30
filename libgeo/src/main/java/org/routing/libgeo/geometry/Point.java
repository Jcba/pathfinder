package org.routing.libgeo.geometry;

import java.util.Objects;

import static java.lang.Math.*;

public final class Point implements Geometry {

    private final double lat;
    private final double lon;


    public Point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

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

    @Override
    public Envelope getEnvelope() {
        return calculateEnvelope();
    }

    private Envelope calculateEnvelope() {
        return Envelope.builder()
                .min(this.lat, this.lon)
                .max(this.lat, this.lon)
                .build();
    }

    public double lat() {
        return lat;
    }

    public double lon() {
        return lon;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Point) obj;
        return Double.doubleToLongBits(this.lat) == Double.doubleToLongBits(that.lat) &&
                Double.doubleToLongBits(this.lon) == Double.doubleToLongBits(that.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public String toString() {
        return "Point[" +
                "lat=" + lat + ", " +
                "lon=" + lon + ']';
    }

}
