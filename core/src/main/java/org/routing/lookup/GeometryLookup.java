package org.routing.lookup;

import org.routing.geometries.AbstractGeometry;
import org.routing.model.Edge;

import java.util.List;

public interface GeometryLookup {

    AbstractGeometry<?> findGeometry(Edge edge);

    AbstractGeometry<?> findGeometries(List<Edge> edges);
}
