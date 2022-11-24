package org.routing.geometries;

import java.util.Properties;

public record Feature(String id, Geometry geometry, Properties properties) {
}
