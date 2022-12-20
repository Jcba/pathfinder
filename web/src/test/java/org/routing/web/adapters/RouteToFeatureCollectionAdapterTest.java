package org.routing.web.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.routing.geometries.Point;
import org.routing.model.Edge;
import org.routing.model.Node;
import org.routing.storage.GeometryStore;

class RouteToFeatureCollectionAdapterTest {

    private RouteToFeatureCollectionAdapter fixture;

    @Mock
    private GeometryStore<Edge> geometryLookup;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fixture = new RouteToFeatureCollectionAdapter(geometryLookup);
    }

    @Test
    public void adaptRoute_shouldReturn() {
        Node start = new Node(0, new Point(0.0f, 1.0f));
        Node destination = new Node(1, new Point(5.0f, 5.0f));
//        Graph graph = TestGraphs.createConnectedGraphWithDepth3(start, destination);
//
//        AStarPathSearch search = new AStarPathSearch(graph);
//
//        Route route = search.route(start, destination);

//        FeatureCollection result = fixture.apply(route);

//        assertThat(result).isEqualTo(new FeatureCollection(List.of(new Feature(UUID.randomUUID().toString(), null, null))));
    }

}