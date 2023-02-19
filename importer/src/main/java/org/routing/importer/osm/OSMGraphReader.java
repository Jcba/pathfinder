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

        List<NodeStore.Node> nodeList = new ArrayList<>();

        for (int i = 0; i < denseNodes.getIdList().size(); i++) {
            long id = denseNodes.getId(i) + prevId;
            double lat = (parseLat(denseNodes.getLat(i)) + prevLat);
            double lon = (parseLon(denseNodes.getLon(i)) + prevLon);

            nodeList.add(new NodeStore.Node(id, (float) lat, (float) lon, 0));

            prevId = id;
            prevLat = lat;
            prevLon = lon;
        }

        nodeStore.save(nodeList);
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
                splitAndSaveEdge(nodeIdList, expandedWayKeyValues);
            }
        }
    }

    private void splitAndSaveEdge(List<Long> nodeIdList, Map<String, List<String>> expandedWayKeyValues) {
        List<NodeStore.Node> nodes = nodeStore.getAll(nodeIdList);

        List<NodeStore.Node> nodesInEdge = new ArrayList<>();

        for (NodeStore.Node node : nodes) {
            if (node.degree() > 0 && nodesInEdge.size() > 1) {
                nodesInEdge.add(node);
                saveEdge(nodesInEdge, expandedWayKeyValues);
                nodesInEdge = new ArrayList<>();
            }
            nodesInEdge.add(node);
        }

        if (nodesInEdge.size() >= 2) {
            saveEdge(nodesInEdge, expandedWayKeyValues);
        }
    }

    private void saveEdge(List<NodeStore.Node> nodesInEdge, Map<String, List<String>> expandedWayKeyValues) {
        LineString lineString = new LineString(nodesInEdge.stream().map(n -> new Point(n.lat(), n.lon())).toList());

        Node nodeFrom = toNode(nodesInEdge.get(0));
        Node nodeTo = toNode(nodesInEdge.get(nodesInEdge.size() - 1));

        Edge edge = new Edge(edgeIdSequence++, nodeFrom, nodeTo, lineString.getDistance());
        edgeGeometryStore.save(edge, lineString);
        graph.addEdge(edge);

        if (!isTrue(expandedWayKeyValues.get("oneway"))) {
            Edge edgeBack = new Edge(edgeIdSequence++, nodeTo, nodeFrom, lineString.getDistance());
            edgeGeometryStore.save(edgeBack, lineString);
            graph.addEdge(edgeBack);
        }
    }

    private boolean isTrue(List<String> values) {
        if (null == values) {
            return false;
        }
        return values.contains("yes") ||
                values.contains("true") ||
                values.contains("1");
    }

    private Node toNode(NodeStore.Node node) {
        return new Node(node.id(), new Point(node.lat(), node.lon()));
    }

    @Override
    protected void parse(Osmformat.HeaderBlock headerBlock) {
        System.out.println("reading file");
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

