package org.routing.lookup;

import org.routing.geometries.AbstractGeometry;
import org.routing.model.Edge;

public interface GraphLookup {

    Edge findClosestEdge(AbstractGeometry<?> geometry);

}
