package org.routing.geometries;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("featureCollection")
public record FeatureCollection(List<Feature> features) {
}
