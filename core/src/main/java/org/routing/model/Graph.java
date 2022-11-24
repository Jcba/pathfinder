package org.routing.model;

public interface Graph extends Iterable<Node> {

    void addEdge(Edge edge);

    void removeEdge(Edge edge);

    void removeNode(Node node);

    Edge[] getConnections(Node node);

    Edge getConnection(Node from, Node to);

    Node getRandomNode();
}
