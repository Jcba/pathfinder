package org.routing.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.geometries.Point;
import org.routing.model.Node;

class RouteToFeatureCollectionAdapterTest {

    RouteToFeatureCollectionAdapter fixture;

    @BeforeEach
    public void setUp() {
        fixture = new RouteToFeatureCollectionAdapter();
    }

    @Test
    public void adaptRoute_shouldReturn() {
        Node start = new Node(new Point(0.0f, 1.0f));
        Node destination = new Node(new Point(5.0f, 5.0f));
//        Graph graph = TestGraphs.createConnectedGraphWithDepth3(start, destination);
//
//        AStarPathSearch search = new AStarPathSearch(graph);
//
//        Route route = search.route(start, destination);

//        FeatureCollection result = fixture.apply(route);

//        assertThat(result).isEqualTo(new FeatureCollection(List.of(new Feature(UUID.randomUUID().toString(), null, null))));
    }

}