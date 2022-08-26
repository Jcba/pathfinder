package org.routing.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.model.*;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.routing.algorithm.TestGraphs.createConnectedGraphWithDepth3;

class AStarPathSearchTest {

    private AStarPathSearch fixture;

    @BeforeEach
    void setUp() {
        Graph graph = new Graph(new HashMap<>());

        fixture = new AStarPathSearch(graph);
    }

    @Test
    void routeEmptyGraph_shouldReturnEmptyRoute() {
        Node start = new Node(new Point(1.0, 1.0));
        Node destination = new Node(new Point(2.0, 2.0));
        Route route = fixture.route(start, destination);

        assertThat(route).isNull();
    }

    @Test
    void route_shouldVisitStartAndEndNode() {
        Node start = new Node(new Point(0.0, 1.0));
        Node destination = new Node(new Point(5.0, 5.0));
        Graph graph = createConnectedGraphWithDepth3(start, destination);
        AStarPathSearch search = new AStarPathSearch(graph);

        Route route = search.route(start, destination);

        assertThat(route).isNotNull()
                .extracting(Route::getNodesOnRoute).isNotNull();

        List<Node> nodesOnRoute = route.getNodesOnRoute();

        assertThat(nodesOnRoute.get(0)).isEqualTo(destination);
        assertThat(nodesOnRoute.get(nodesOnRoute.size()-1)).isEqualTo(start);
    }
}