package org.routing.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Route;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.routing.search.TestGraphs.createConnectedGraphWithDepth3;

class AStarPathSearchTest {

    private AStarPathSearch fixture;

    @BeforeEach
    void setUp() {
        Graph graph = new Graph(new ConcurrentHashMap<>());

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
        assertThat(nodesOnRoute.get(nodesOnRoute.size() - 1)).isEqualTo(start);
    }
}