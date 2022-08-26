package org.routing.algorithm;

import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Point;

public class TestGraphs {

    public static Graph createConnectedGraphWithDepth3(Node start, Node destination) {
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
