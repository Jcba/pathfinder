package org.routing.web.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.routing.geometries.FeatureCollection;
import org.routing.importer.OSMImporter;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Route;
import org.routing.search.AStarPathSearch;
import org.routing.search.PathSearchAlgorithm;
import org.routing.web.adapters.RouteToFeatureCollectionAdapter;

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

    public Graph loadGraph() throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("flevoland-latest.osm.pbf");
        if (null != resource) {
            Path networkPath = Path.of(resource.toURI());
            OSMImporter osmImporter = new OSMImporter();
            osmImporter.importFromFile(networkPath);
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

        // TODO: add geometry lookup
//        return new RouteToFeatureCollectionAdapter(new SqliteGeometryLookup()).apply(route);
        return null;
    }
}
