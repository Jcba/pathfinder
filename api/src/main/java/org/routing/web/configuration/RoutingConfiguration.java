package org.routing.web.configuration;

import org.routing.importer.GraphImporter;
import org.routing.importer.osm.OSMImporter;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.MemoryGraph;
import org.routing.search.AStarPathSearch;
import org.routing.search.PathSearchAlgorithm;
import org.routing.storage.DatabaseConfiguration;
import org.routing.storage.GeometryLookup;
import org.routing.storage.GeometryStore;
import org.routing.storage.H2GisGeometryStore;
import org.routing.web.model.NetworkLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

@Configuration
public class RoutingConfiguration {

    @Value("${network.name}")
    private String network;

    @Value("${network.load}")
    private NetworkLoader networkLoader;

    private H2GisGeometryStore<Edge> geometryStore;

    @Bean
    public GeometryStore<Edge> edgeGeometryStore() {
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(
                "jdbc:h2:~/geometry.db",
                60,
                true,
                true
        );
        geometryStore = new H2GisGeometryStore<>(databaseConfiguration);
        return geometryStore;
    }

    @Bean
    public GeometryLookup geometryLookup() {
        return geometryStore;
    }

    @Bean
    public Graph memoryGraph(GeometryStore<Edge> edgeGeometryStore) throws URISyntaxException {
        if (networkLoader == NetworkLoader.ON_STARTUP) {
            URL resource = getClass().getClassLoader().getResource(network);
            Graph graph = new MemoryGraph();
            if (null != resource) {
                Path networkPath = Path.of(resource.toURI());
                GraphImporter osmImporter = new OSMImporter(edgeGeometryStore);
                osmImporter.importFromFile(networkPath, graph);
            }
            return graph;
        }
        return new MemoryGraph();
    }

    @Bean
    public PathSearchAlgorithm pathSearchAlgorithm(Graph graph) {
        return new AStarPathSearch(graph);
    }

}
