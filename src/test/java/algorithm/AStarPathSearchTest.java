package algorithm;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

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
        fixture.route(start, destination);
    }

    @Test
    void route() {
        Node start = new Node(new Point(0.0, 1.0));
        Node destination = new Node(new Point(5.0, 5.0));
        Graph graph = createGraph(start, destination);
        AStarPathSearch search = new AStarPathSearch(graph);

        Route route = search.route(start, destination);

        System.out.println(route);
    }

    private Graph createGraph(Node start, Node destination) {
        Graph graph = new Graph();
        Node node1 = new Node(new Point(1.0, 1.0));
        Node node2 = new Node(new Point(1.0, 2.0));
        Node node3 = new Node(new Point(1.0, 3.0));
        Node node4 = new Node(new Point(2.0, 3.0));
        Node node5 = new Node(new Point(2.0, 4.0));
        graph.addEdge(new Edge(node1, node2, 1.0));
        graph.addEdge(new Edge(node1, node3, 1.0));
        graph.addEdge(new Edge(node2, node3, 1.0));
        graph.addEdge(new Edge(node1, node4, 1.0));
        graph.addEdge(new Edge(node3, node4, 1.0));
        graph.addEdge(new Edge(node4, node5, 1.0));
        graph.addEdge(new Edge(start, node1, 1.0));
        graph.addEdge(new Edge(node5, destination, 1.0));
        return graph;
    }
}