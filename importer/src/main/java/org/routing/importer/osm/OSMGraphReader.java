package org.routing.importer.osm;

import crosby.binary.Osmformat;
import org.routing.geometries.LineString;
import org.routing.geometries.Point;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.storage.GeometryStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OSMGraphReader extends AbstractOSMParser {
    private final NodeStore nodeStore;
    private final GeometryStore<Edge> edgeGeometryStore;
    private final Graph graph;
    private long edgeIdSequence = 0;

    public OSMGraphReader(NodeStore nodeStore, Graph graph, GeometryStore<Edge> edgeGeometryStore) {
        this.nodeStore = nodeStore;
        this.edgeGeometryStore = edgeGeometryStore;
        this.graph = graph;
        nodeStore.open();
    }

    @Override
    protected void parseRelations(List<Osmformat.Relation> list) {
        //do nothing
    }

    @Override
    protected void parseDense(Osmformat.DenseNodes denseNodes) {
        long prevId = 0;
        double prevLat = 0;
        double prevLon = 0;
        for (int i = 0; i < denseNodes.getIdList().size(); i++) {
            long id = denseNodes.getId(i) + prevId;
            double lat = (parseLat(denseNodes.getLat(i)) + prevLat);
            double lon = (parseLon(denseNodes.getLon(i)) + prevLon);

            saveNode(id, lat, lon);

            prevId = id;
            prevLat = lat;
            prevLon = lon;
        }
    }

    private void saveNode(long nodeId, double lat, double lon) {
        nodeStore.save(nodeId, lat, lon);
    }

    @Override
    protected void parseNodes(List<Osmformat.Node> list) {
        // do nothing
    }

    @Override
    protected void parseWays(List<Osmformat.Way> list) {
        for (Osmformat.Way way : list) {
            Map<String, List<String>> expandedWayKeyValues = getExpandedKeyVals(way.getKeysList(), way.getValsList());

            if (shouldParseWayType(expandedWayKeyValues)) {
                List<Long> refsList = way.getRefsList();
                List<Long> nodeIdList = new ArrayList<>();
                long prevNodeReference = 0;
                for (long ref : refsList) {
                    long nodeId = ref + prevNodeReference;

                    nodeIdList.add(nodeId);

                    prevNodeReference = nodeId;
                }
                splitAndSaveEdge(nodeIdList);
            }
        }
    }

    private void splitAndSaveEdge(List<Long> nodeIdList) {
        List<NodeStore.Node> nodes = nodeStore.getAll(nodeIdList);

        List<NodeStore.Node> nodesInEdge = new ArrayList<>();

        for (NodeStore.Node node : nodes) {
            if (node.degree() > 1 && nodesInEdge.size() > 0) {
                nodesInEdge.add(node);
                saveEdge(nodesInEdge);
                nodesInEdge = new ArrayList<>();
            }
            nodesInEdge.add(node);
        }

        if (nodesInEdge.size() >= 2) {
            saveEdge(nodesInEdge);
        }
    }

    private void saveEdge(List<NodeStore.Node> nodesInEdge) {
        LineString lineString = new LineString(nodesInEdge.stream().map(n -> new Point(n.lat(), n.lon())).toList());

        Node nodeFrom = toNode(nodesInEdge.get(0));
        Node nodeTo = toNode(nodesInEdge.get(nodesInEdge.size()-1));

        Edge edge = new Edge(edgeIdSequence++, nodeFrom, nodeTo, lineString.getDistance());

        edgeGeometryStore.save(edge, lineString);

        graph.addEdge(edge);
    }

    private Node toNode(NodeStore.Node node) {
        return new Node(node.id(), new Point(node.lat(), node.lon()));
    }

    @Override
    protected void parse(Osmformat.HeaderBlock headerBlock) {
    }

    @Override
    public void complete() {
        System.out.printf("""
                ------------------------------------------------
                Completed reading edges from OSM PBF
                ------------------------------------------------
                %n""");
        nodeStore.close();
    }
}

