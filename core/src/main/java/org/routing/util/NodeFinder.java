package org.routing.util;

import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeFinder {
    private static final Logger log = LoggerFactory.getLogger(NodeFinder.class);

    private final Graph graph;

    public NodeFinder(Graph graph) {
        this.graph = graph;
    }

    public Node findNearestNode(Point point) {
        return null;
    }

}
