package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonGetter;

public abstract class AbstractGeometry<T> {

    /**
     * The type according to the GeoJSON specification
     *
     * @return the GeoJSON Geometry type
     */
    @JsonGetter("type")
    public abstract String getType();

    /**
     * The geometry coordinates
     *
     * @return the geometry coordinates
     */
    @JsonGetter("coordinates")
    public abstract T getCoordinates();

}
