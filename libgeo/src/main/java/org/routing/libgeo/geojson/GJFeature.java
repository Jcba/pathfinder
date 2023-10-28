package org.routing.libgeo.geojson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Properties;

/**
 * GeoJSON feature. See <a href="https://www.rfc-editor.org/rfc/rfc7946#section-3.2">GeoJSON Feature specification</a>
 *
 * @param id               Required, A unique id
 * @param GJAbstractGeometry Required, a geometry
 * @param properties       Required, additional properties
 */
@JsonPropertyOrder({
        "type",
        "properties",
        "geometry"
})
public record GJFeature(String id, @JsonGetter("geometry")
GJAbstractGeometry<?> GJAbstractGeometry, Properties properties) {

    @JsonGetter("type")
    String getType() {
        return "Feature";
    }
}
