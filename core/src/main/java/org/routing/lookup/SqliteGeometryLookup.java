package org.routing.lookup;

import org.routing.geometries.AbstractGeometry;
import org.routing.model.Edge;

import java.util.List;

public class SqliteGeometryLookup implements GeometryLookup {

    @Override
    public AbstractGeometry<?> findGeometry(Edge edge) {
        return null;
    }

    @Override
    public AbstractGeometry<?> findGeometries(List<Edge> edges) {
        return null;
    }
}
