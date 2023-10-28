package org.routing.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.libgeo.geometry.Point;
import org.routing.model.Graph;
import org.routing.model.MemoryGraph;
import org.routing.model.Node;
import org.routing.model.Route;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.routing.search.TestGraphs.createConnectedGraphWithDepth3;

class AStarPathSearchTest {

    private AStarPathSearch fixture;

    @BeforeEach
    void setUp() {
        MemoryGraph memoryGraph = new MemoryGraph();

        fixture = new AStarPathSearch(memoryGraph);
    }

    @Test
    void routeEmptyGraph_shouldReturnEmptyRoute() {
        Node start = new Node(10, new Point(1.0f, 1.0f));
        Node destination = new Node(11, new Point(2.0f, 2.0f));
        Route route = fixture.route(start, destination);

        assertThat(route).isNull();
    }

    @Test
    void route_shouldVisitStartAndEndNode() {
        Node start = new Node(10, new Point(0.0f, 1.0f));
        Node destination = new Node(11, new Point(5.0f, 5.0f));
        Graph memoryGraph = createConnectedGraphWithDepth3(start, destination);
        AStarPathSearch search = new AStarPathSearch(memoryGraph);

        Route route = search.route(start, destination);

        assertThat(route).isNotNull()
                .extracting(Route::getNodesOnRoute).isNotNull();

        List<Node> nodesOnRoute = route.getNodesOnRoute();

        assertThat(nodesOnRoute.get(0)).isEqualTo(destination);
        assertThat(nodesOnRoute.get(nodesOnRoute.size() - 1)).isEqualTo(start);
    }
}