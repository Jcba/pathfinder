package org.routing.search;

import org.routing.geometries.Point;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.MemoryGraph;
import org.routing.model.Node;

public class TestGraphs {

    public static Graph createConnectedGraphWithDepth3(Node start, Node destination) {
        MemoryGraph memoryGraph = new MemoryGraph();
        Node node1 = new Node(0, new Point(1.0f, 1.0f));
        Node node2 = new Node(1, new Point(1.0f, 2.0f));
        Node node3 = new Node(2, new Point(1.0f, 3.0f));
        Node node4 = new Node(3, new Point(2.0f, 3.0f));
        Node node5 = new Node(4, new Point(2.0f, 4.0f));
        memoryGraph.addEdge(new Edge(0, node1, node2, 1.0));
        memoryGraph.addEdge(new Edge(1, node1, node3, 1.0));
        memoryGraph.addEdge(new Edge(2, node2, node3, 1.0));
        memoryGraph.addEdge(new Edge(3, node1, node4, 1.0));
        memoryGraph.addEdge(new Edge(4, node3, node4, 1.0));
        memoryGraph.addEdge(new Edge(5, node4, node5, 1.0));
        memoryGraph.addEdge(new Edge(6, start, node1, 1.0));
        memoryGraph.addEdge(new Edge(7, node5, destination, 1.0));
        return memoryGraph;
    }
}
