package org.routing.model;

import org.routing.storage.KeyProvider;

public interface Graph {

    void addEdge(Edge edge);

    Edge[] getConnections(Node node);

    Edge getConnection(Node from, Node to);

    Node getRandomNode();

    Edge findEdge(KeyProvider key);
}
