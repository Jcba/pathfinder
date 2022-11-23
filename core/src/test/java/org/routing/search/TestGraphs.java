package org.routing.search;

import org.routing.geometries.Point;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.MemoryGraph;
import org.routing.model.Node;

public class TestGraphs {

    public static Graph createConnectedGraphWithDepth3(Node start, Node destination) {
        MemoryGraph memoryGraph = new MemoryGraph();
        Node node1 = new Node(new Point(1.0f, 1.0f));
        Node node2 = new Node(new Point(1.0f, 2.0f));
        Node node3 = new Node(new Point(1.0f, 3.0f));
        Node node4 = new Node(new Point(2.0f, 3.0f));
        Node node5 = new Node(new Point(2.0f, 4.0f));
        memoryGraph.addEdge(new Edge(node1, node2, 1.0));
        memoryGraph.addEdge(new Edge(node1, node3, 1.0));
        memoryGraph.addEdge(new Edge(node2, node3, 1.0));
        memoryGraph.addEdge(new Edge(node1, node4, 1.0));
        memoryGraph.addEdge(new Edge(node3, node4, 1.0));
        memoryGraph.addEdge(new Edge(node4, node5, 1.0));
        memoryGraph.addEdge(new Edge(start, node1, 1.0));
        memoryGraph.addEdge(new Edge(node5, destination, 1.0));
        return memoryGraph;
    }
}
