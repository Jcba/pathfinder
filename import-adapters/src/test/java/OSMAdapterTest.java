import algorithm.AStarPathSearch;
import api.PathSearchAlgorithm;
import model.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

class OSMAdapterTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void readFlevoland() throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource("flevoland-latest.osm.pbf");
        if (null != resource) {
            Path flevoland = Path.of(resource.toURI());
            OSMAdapter adapter = new OSMAdapter(flevoland);
            PathSearchAlgorithm search = new AStarPathSearch(adapter);
            Route route = search.route(adapter.getRandomNode(), adapter.getRandomNode());

            System.out.println(route.getNodesOnRoute());
        }
    }
}