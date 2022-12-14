package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Properties;

@JsonPropertyOrder({
        "type",
        "properties",
        "geometry"
})
public record Feature(String id, @JsonGetter("geometry")
AbstractGeometry<?> abstractGeometry, Properties properties) {

    @JsonGetter("type")
    String getType() {
        return "Feature";
    }
}
