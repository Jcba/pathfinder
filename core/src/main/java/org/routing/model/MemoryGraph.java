package org.routing.model;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class MemoryGraph implements Graph {

    private final ConcurrentMap<Node, Edge[]> adjacencyListMap;

    public MemoryGraph(ConcurrentMap<Node, Edge[]> adjacencyListMap) {
        this.adjacencyListMap = adjacencyListMap;
    }

    public MemoryGraph() {
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

    @NotNull
    @Override
    public Iterator<Node> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Node> action) {
        Graph.super.forEach(action);
    }

    @Override
    public Spliterator<Node> spliterator() {
        return Graph.super.spliterator();
    }
}
