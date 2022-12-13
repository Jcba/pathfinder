package org.routing.web.configuration;

import org.routing.geometries.FeatureCollection;
import org.routing.importer.OSMImporter;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Route;
import org.routing.search.AStarPathSearch;
import org.routing.search.PathSearchAlgorithm;
import org.routing.storage.DatabaseConfiguration;
import org.routing.storage.GeometryStore;
import org.routing.storage.H2GisGeometryStore;
import org.routing.web.adapters.RouteToFeatureCollectionAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

@Configuration
public class RoutingConfiguration {

    private final PathSearchAlgorithm search;
    private final Graph graph;
    private final GeometryStore<Edge> edgeGeometryStore;

    @Value("${network.name}")
    private String network;

    public RoutingConfiguration() throws URISyntaxException {
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(
                "jdbc:h2:~/geometry.db",
                60,
                true,
                true
        );
        edgeGeometryStore = new H2GisGeometryStore<>(databaseConfiguration);
        graph = loadGraph();
        search = getPathSearchAlgorithm(graph);
    }

    public Graph loadGraph() throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("flevoland-latest.osm.pbf");
        if (null != resource) {
            Path networkPath = Path.of(resource.toURI());
            OSMImporter osmImporter = new OSMImporter(edgeGeometryStore);
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

        return new RouteToFeatureCollectionAdapter(edgeGeometryStore).apply(route);
    }
}
