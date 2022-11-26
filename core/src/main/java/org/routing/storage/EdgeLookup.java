package org.routing.storage;

import org.routing.geometries.AbstractGeometry;
import org.routing.model.Edge;

public interface EdgeLookup {

    Edge findClosest(AbstractGeometry<?> geometry);

}
