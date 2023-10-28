package org.routing.libgeo.geojson;

/**
 * Point class. Jackson output marshals to a GeoJSON Point.
 */
public class GJPoint extends GJAbstractGeometry<Double[]> {

    private Double[] coordinates;

    public GJPoint() {
        // default constructor
    }

    public GJPoint(org.routing.libgeo.geometry.Point point) {
        this(point.lat(), point.lon());
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

}
