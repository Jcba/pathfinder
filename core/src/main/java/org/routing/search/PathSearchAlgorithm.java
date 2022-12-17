package org.routing.search;

import org.routing.model.Node;
import org.routing.model.Route;

public interface PathSearchAlgorithm {

    /**
     * Calculate the fastest route from a start Node to an end Node
     *
     * @param start       the start Node
     * @param destination the end Node
     * @return the fastest route
     */
    Route route(Node start, Node destination);
}
