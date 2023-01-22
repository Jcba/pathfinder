package org.routing.model;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.routing.storage.serializer.EdgeArraySerializer;
import org.routing.storage.serializer.EdgeSerializer;
import org.routing.storage.serializer.NodeSerializer;
import org.routing.storage.KeyProvider;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class MemoryGraph implements Graph {

    private final Map<Node, Edge[]> adjacencyListMap;
    private final Map<Long, Edge> edgeIdEdgeMap;

    private DB db;

    public MemoryGraph(ConcurrentMap<Node, Edge[]> adjacencyListMap) {
        this.adjacencyListMap = adjacencyListMap;
        this.edgeIdEdgeMap = new HashMap<>();
    }

    public MemoryGraph() {
        db = DBMaker.fileDB("graph.db").make();
        adjacencyListMap = db.hashMap("graphMap",
                        new NodeSerializer(),
                        new EdgeArraySerializer(new NodeSerializer()))
                .createOrOpen();
        edgeIdEdgeMap = db.hashMap("edgeIdMap",
                        Serializer.LONG,
                        new EdgeSerializer(new NodeSerializer()))
                .createOrOpen();
    }

    public void destroy() {
        if (null != db) {
            db.close();
        }
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
