package org.routing.web.configuration;

import org.routing.adapter.GeoJSON;
import org.routing.adapter.OSMAdapter;
import org.routing.algorithm.AStarPathSearch;
import org.routing.api.PathSearchAlgorithm;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Route;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.stream.Collectors;

@Singleton
public class RoutingConfiguration {

    private final PathSearchAlgorithm search;
    private final Graph graph;

    @Inject
    private NetworkConfiguration networkConfiguration;

    public RoutingConfiguration() throws IOException {
        graph = loadGraph();
        search = getPathSearchAlgorithm(graph);
    }

    public Graph loadGraph() throws IOException {
        return new OSMAdapter(networkConfiguration.name());
    }

    public PathSearchAlgorithm getPathSearchAlgorithm(Graph graph) {
        return new AStarPathSearch(graph);
    }

    public String getRoute() {
        Node start = graph.getRandomNode();
        Node destination = graph.getRandomNode();
        Route route = search.route(start, destination);
        return GeoJSON.asLineString(route.getNodesOnRoute().stream().map(Node::getCoordinate).collect(Collectors.toList()));
    }
}
