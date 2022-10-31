package org.routing.util;

import org.routing.geometries.Point;
import org.routing.model.MemoryGraph;
import org.routing.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeFinder {
    private static final Logger log = LoggerFactory.getLogger(NodeFinder.class);

    private final MemoryGraph memoryGraph;

    public NodeFinder(MemoryGraph memoryGraph) {
        this.memoryGraph = memoryGraph;
    }

    public Node findNearestNode(Point point) {
        return null;
    }

}
