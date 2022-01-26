package org.routing.api;

import org.routing.model.Node;
import org.routing.model.Route;

public interface PathSearchAlgorithm {

    Route route(Node start, Node destination);
}
