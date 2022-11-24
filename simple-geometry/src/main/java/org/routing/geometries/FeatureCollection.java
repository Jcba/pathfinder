package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonRootName("featureCollection")
@JsonTypeName("featureCollection")
public record FeatureCollection(List<Feature> features) {
}
