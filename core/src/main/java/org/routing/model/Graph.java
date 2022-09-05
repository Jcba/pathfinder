package org.routing.model;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;

public class Graph {

    private final ConcurrentMap<Node, Edge[]> adjacencyListMap;

    public Graph(ConcurrentMap<Node, Edge[]> adjacencyListMap) {
        this.adjacencyListMap = adjacencyListMap;
    }

    public Graph() {
        DB db = DBMaker.memoryDB().make();
        adjacencyListMap = (ConcurrentMap<Node, Edge[]>) db
                .hashMap("map")
                .createOrOpen();
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
        return nodeList.get(r.nextInt(nodeList.size() / 10));
    }
}
