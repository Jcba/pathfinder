package org.routing.model;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * Memory-efficient graph model. Can be used for pre-processing steps
 *
 * Not suitable for graph search; because access is too slow.
 */
public class FileGraph implements Graph {

    private final ConcurrentMap<Node, Edge[]> adjacencyListMap;

    public FileGraph() {
        DB db = DBMaker.tempFileDB().make();
        adjacencyListMap = (ConcurrentMap<Node, Edge[]>) db
                .hashMap("map")
                .createOrOpen();
    }

    @Override
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

    @Override
    public void removeEdge(Edge edge) {

    }

    @Override
    public void removeNode(Node node) {
        this.adjacencyListMap.remove(node);
    }

    @Override
    public Edge[] getConnections(Node node) {
        if (adjacencyListMap.containsKey(node)) {
            return adjacencyListMap.get(node);
        }
        return new Edge[0];
    }

    @Override
    public Edge getConnection(Node from, Node to) {
        return null;
    }

    @NotNull
    @Override
    public Iterator<Node> iterator() {
        return this.adjacencyListMap.keySet().iterator();
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
