import algorithm.AStarPathSearch;
import api.PathSearchAlgorithm;
import model.Node;
import model.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Collectors;

class OSMAdapterTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void readFlevoland() throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource("flevoland-latest.osm.pbf");
        if (null != resource) {
            Path flevoland = Path.of(resource.toURI());
            OSMAdapter osmGraph = new OSMAdapter(flevoland);
            PathSearchAlgorithm search = new AStarPathSearch(osmGraph);
            Route route = search.route(osmGraph.getRandomNode(), osmGraph.getRandomNode());

            System.out.println(GeoJSON.asGeojson(route.getNodesOnRoute().stream().map(Node::getCoordinate).collect(Collectors.toList())));
        }
    }
}