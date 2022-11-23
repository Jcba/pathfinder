package org.routing.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.geometries.Feature;
import org.routing.geometries.FeatureCollection;
import org.routing.geometries.Point;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Route;
import org.routing.search.AStarPathSearch;
import org.routing.search.TestGraphs;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.routing.search.TestGraphs.createConnectedGraphWithDepth3;

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
        Graph graph = createConnectedGraphWithDepth3(start, destination);

        AStarPathSearch search = new AStarPathSearch(graph);

        Route route = search.route(start, destination);

//        FeatureCollection result = fixture.apply(route);

//        assertThat(result).isEqualTo(new FeatureCollection(List.of(new Feature(UUID.randomUUID().toString(), null, null))));
    }

}