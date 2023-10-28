package org.routing.libgeo.geojson;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.lang.Math.*;

/**
 * Point class. Jackson output marshals to a GeoJSON Point.
 */
public class GJPoint extends GJAbstractGeometry<Double[]> {

    private Double[] coordinates;

    public GJPoint() {
        // default constructor
    }

    public GJPoint(double lat, double lon) {
        this.coordinates = new Double[]{lon, lat};
    }

    public String getType() {
        return "Point";
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    @JsonIgnore
    public double getLon() {
        return coordinates[0];
    }

    @JsonIgnore
    public double getLat() {
        return coordinates[1];
    }

    @JsonIgnore
    public float distance(GJPoint other) {
        var earthRadius = 6371000.0; //meters
        var dLat = Math.toRadians((other.getLat() - this.getLat()));
        var dLng = Math.toRadians((other.getLon() - this.getLon()));
        var a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(this.getLat())) *
                        cos(Math.toRadians(other.getLat())) *
                        sin(dLng / 2) * sin(dLng / 2);
        var c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return (float) (earthRadius * c);
    }
}
