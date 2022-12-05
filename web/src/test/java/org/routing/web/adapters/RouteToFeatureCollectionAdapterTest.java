package org.routing.web.adapters;

import io.quarkus.test.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.routing.geometries.Point;
import org.routing.storage.GeometryLookup;
import org.routing.model.Node;

class RouteToFeatureCollectionAdapterTest {

    private RouteToFeatureCollectionAdapter fixture;

    @Mock
    private GeometryLookup geometryLookup;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fixture = new RouteToFeatureCollectionAdapter(geometryLookup);
    }

    @Test
    public void adaptRoute_shouldReturn() {
        Node start = new Node(id, new Point(0.0f, 1.0f));
        Node destination = new Node(id, new Point(5.0f, 5.0f));
//        Graph graph = TestGraphs.createConnectedGraphWithDepth3(start, destination);
//
//        AStarPathSearch search = new AStarPathSearch(graph);
//
//        Route route = search.route(start, destination);

//        FeatureCollection result = fixture.apply(route);

//        assertThat(result).isEqualTo(new FeatureCollection(List.of(new Feature(UUID.randomUUID().toString(), null, null))));
    }

}