package org.routing.model;

import java.util.*;

public class Graph {

    private final Map<Node, Edge[]> adjacencyListMap;

    public Graph(Map<Node, Edge[]> adjacencyListMap) {
        this.adjacencyListMap = adjacencyListMap;
    }

    public Graph() {
        adjacencyListMap = new HashMap<>();
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
    }

    public Edge[] getConnections(Node node) {
        if (adjacencyListMap.containsKey(node)) {
            return adjacencyListMap.get(node);
        }
        return new Edge[0];
    }

    public Node getRandomNode() {
        Random r = new Random();
        List<Node> nodeList = new ArrayList<>(adjacencyListMap.keySet());
        return nodeList.get(r.nextInt(nodeList.size()/10));
    }
}
