package org.routing.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.adapters.RouteToFeatureCollectionAdapter;
import org.routing.model.Route;
import org.routing.search.AStarPathSearch;
import org.routing.search.PathSearchAlgorithm;

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
            OSMAdapter osmGraph = new OSMAdapter(flevoland);
            PathSearchAlgorithm search = new AStarPathSearch(osmGraph);
            Route route = search.route(osmGraph.getRandomNode(), osmGraph.getRandomNode());

            System.out.println(new RouteToFeatureCollectionAdapter().apply(route));
        }
    }
}