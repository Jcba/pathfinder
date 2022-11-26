package org.routing.model;

public interface Graph {

    void addEdge(Edge edge);

    Edge[] getConnections(Node node);

    Edge getConnection(Node from, Node to);

    Node getRandomNode();
}
