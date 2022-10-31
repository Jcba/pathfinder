package org.routing.web.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.routing.adapter.GeoJSON;
import org.routing.adapter.OSMAdapter;
import org.routing.search.AStarPathSearch;
import org.routing.search.PathSearchAlgorithm;
import org.routing.model.MemoryGraph;
import org.routing.model.Node;
import org.routing.model.Route;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

@Singleton
public class RoutingConfiguration {

    private final PathSearchAlgorithm search;
    private final MemoryGraph memoryGraph;

    @ConfigProperty(name = "network.name")
    String network;

    public RoutingConfiguration() throws IOException, URISyntaxException {
        memoryGraph = loadGraph();
        search = getPathSearchAlgorithm(memoryGraph);
    }

    public MemoryGraph loadGraph() throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("flevoland-latest.osm.pbf");
        if (null != resource) {
            Path networkPath = Path.of(resource.toURI());
            return new OSMAdapter(networkPath);
        }
        return null;
    }

    public PathSearchAlgorithm getPathSearchAlgorithm(MemoryGraph memoryGraph) {
        return new AStarPathSearch(memoryGraph);
    }

    public String getRoute() {
        Node start = memoryGraph.getRandomNode();
        Node destination = memoryGraph.getRandomNode();
        Route route = search.route(start, destination);
        return GeoJSON.asLineString(route.getNodesOnRoute().stream().map(Node::coordinate).toList());
    }
}
