package org.routing.model;

import org.routing.storage.KeyProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple all-in-memory Graph implementation
 */
public class MemoryGraph implements Graph {

    private final Map<Node, Edge[]> adjacencyListMap;
    private final Map<Long, Edge> edgeIdEdgeMap;


    public MemoryGraph() {
        adjacencyListMap = new HashMap<>();
        edgeIdEdgeMap = new HashMap<>();
    }

    public void addEdge(Edge edge) {
        Node from = edge.getFrom();

        List<Edge> edgeList = new ArrayList<>();

        if (adjacencyListMap.containsKey(from)) {
            Edge[] edges = adjacencyListMap.get(from);
            edgeList = new ArrayList<>(List.of(edges));
        }

        edgeList.add(edge);
        Edge[] value = edgeList.toArray(Edge[]::new);
        adjacencyListMap.put(from, value);
        edgeIdEdgeMap.put(edge.getId(), edge);
    }

    public Edge[] getConnections(Node node) {
        if (adjacencyListMap.containsKey(node)) {
            return adjacencyListMap.get(node);
        }
        return new Edge[0];
    }

    public Edge getConnection(Node from, Node to) {
        for (Edge connection : getConnections(from)) {
            if (connection.getTo().equals(to)) {
                return connection;
            }
        }
        return null;
    }

    @Override
    public Edge findEdge(KeyProvider key) {
        if (key.getType().equals(Edge.KEY_TYPE)) {
            return edgeIdEdgeMap.get(key.getId());
        }
        return null;
    }
}
