package org.routing.libgeo.geojson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * GeoJSON FeatureCollection. See
 * <a href="https://www.rfc-editor.org/rfc/rfc7946#section-3.3">GeoJSON FeatureCollection specification</a>
 *
 * @param GJFeatures Required: a list of features
 */
@JsonPropertyOrder({
        "type",
        "features"
})
public record GJFeatureCollection(List<GJFeature> GJFeatures) {

    @JsonGetter("type")
    String getType() {
        return "FeatureCollection";
    }
}
