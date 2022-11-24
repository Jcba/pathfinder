package org.routing.web.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.routing.adapter.OSMAdapter;
import org.routing.adapters.RouteToFeatureCollectionAdapter;
import org.routing.geometries.FeatureCollection;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Route;
import org.routing.search.AStarPathSearch;
import org.routing.search.PathSearchAlgorithm;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

@Singleton
public class RoutingConfiguration {

    private final PathSearchAlgorithm search;
    private final Graph graph;

    @ConfigProperty(name = "network.name")
    String network;

    public RoutingConfiguration() throws IOException, URISyntaxException {
        graph = loadGraph();
        search = getPathSearchAlgorithm(graph);
    }

    public Graph loadGraph() throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("flevoland-latest.osm.pbf");
        if (null != resource) {
            Path networkPath = Path.of(resource.toURI());
            return new OSMAdapter(networkPath);
        }
        return null;
    }

    public PathSearchAlgorithm getPathSearchAlgorithm(Graph graph) {
        return new AStarPathSearch(graph);
    }

    public FeatureCollection getRoute() {
        Node start = graph.getRandomNode();
        Node destination = graph.getRandomNode();
        Route route = search.route(start, destination);

        return new RouteToFeatureCollectionAdapter().apply(route);
    }
}
