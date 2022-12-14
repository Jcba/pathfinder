package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "type",
        "features"
})
public record FeatureCollection(List<Feature> features) {

    @JsonGetter("type")
    String getType() {
        return "FeatureCollection";
    }
}
