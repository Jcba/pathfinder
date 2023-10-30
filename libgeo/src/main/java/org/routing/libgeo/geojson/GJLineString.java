package org.routing.libgeo.geojson;

import org.routing.libgeo.geometry.LineString;

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

    public GJLineString(LineString lineString) {
        type = "LineString";

        this.coordinates = lineString.points().stream()
                .map(p -> new Double[]{p.lon(), p.lat()})
                .toArray(Double[][]::new);
    }

    public GJLineString(List<GJPoint> GJPoints) {
        this.coordinates = GJPoints.stream().map(GJPoint::getCoordinates).toArray(Double[][]::new);
        type = "LineString";
    }

    @Override
    public String getType() {
        return "LineString";
    }

    @Override
    public Double[][] getCoordinates() {
        return coordinates;
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
