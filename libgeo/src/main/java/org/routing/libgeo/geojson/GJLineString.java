package org.routing.libgeo.geojson;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;

/**
 * LineString class. Jackson output marshals to a GeoJSON LineString.
 */
public class GJLineString extends GJAbstractGeometry<Double[][]> {

    private Double[][] coordinates;

    public GJLineString() {
        // default constructor
        type = "LineString";
    }

    public GJLineString(List<GJPoint> GJPoints) {
        this.coordinates = GJPoints.stream().map(GJPoint::getCoordinates).toArray(Double[][]::new);
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
    public List<GJPoint> getPoints() {
        return Arrays.stream(getCoordinates()).map(c -> new GJPoint(c[1], c[0])).toList();
    }

    @JsonIgnore
    public double getDistance() {
        double distance = 0;
        List<GJPoint> GJPoints = getPoints();
        if (GJPoints.size() < 2) {
            return distance;
        }
        for (int i = 0; i < GJPoints.size() - 1; i++) {
            distance += GJPoints.get(i).distance(GJPoints.get(i + 1));
        }
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GJLineString that = (GJLineString) o;

        return Arrays.deepEquals(getCoordinates(), that.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getCoordinates());
    }
}
