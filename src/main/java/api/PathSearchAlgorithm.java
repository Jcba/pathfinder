package api;

import model.Node;
import model.Route;

public interface PathSearchAlgorithm {

    Route route(Node start, Node destination);
}
