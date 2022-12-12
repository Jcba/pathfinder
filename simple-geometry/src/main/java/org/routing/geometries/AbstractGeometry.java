package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonPropertyOrder({
        "type",
        "coordinates"
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LineString.class, name = "LineString"),
        @JsonSubTypes.Type(value = Point.class, name = "Point")
})
public abstract class AbstractGeometry<T> {

    protected String type;

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
