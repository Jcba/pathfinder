package org.routing.importer.osm;

import crosby.binary.Osmformat;
import org.routing.geometries.LineString;
import org.routing.geometries.Point;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.storage.GeometryStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSMGraphParser extends AbstractOSMParser {

    private final Map<Point, Long> pointNodeIdMap = new HashMap<>();
    private final Map<Long, Point> nodeMap = new HashMap<>();
    private final Graph graph;
    private final GeometryStore<Edge> edgeGeometryStore;
    private final Map<Long, Short> nodeDegreeMap;

    private long nodeSequence = 0;
    private long edgeSequence = 0;

    public OSMGraphParser(Graph graph, GeometryStore<Edge> edgeGeometryStore, Map<Long, Short> nodeDegreeMap) {
        this.graph = graph;
        this.edgeGeometryStore = edgeGeometryStore;
        this.nodeDegreeMap = nodeDegreeMap;
    }

    private void storeEdge(Edge edge, LineString lineString) {
        edgeGeometryStore.save(edge, lineString);
        graph.addEdge(edge);
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
            if (!nodeMap.containsKey(id)) {
                nodeMap.put(id, new Point(lat, lon));
            }
            prevId = id;
            prevLat = lat;
            prevLon = lon;
        }
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
                List<Point> points = new ArrayList<>();
                long prevNodeReference = 0;
                for (long ref : refsList) {
                    long nodeId = ref + prevNodeReference;
                    Point node = nodeMap.get(nodeId);
                    points.add(node);
                    if (nodeDegreeMap.get(nodeId) > 1 && points.size() > 1) {
                        addEdge(points, expandedWayKeyValues);
                        points = new ArrayList<>();
                        points.add(node);
                    }
                    prevNodeReference = nodeId;
                }
            }
        }
    }

    private void addEdge(List<Point> points, Map<String, List<String>> expandedWayKeyValues) {
        if (points.size() >= 2) {
            LineString lineString = new LineString(points);

            long firstNodeId = pointNodeIdMap.computeIfAbsent(points.get(0), v -> nodeSequence++);
            long secondNodeId = pointNodeIdMap.computeIfAbsent(points.get(points.size() - 1), v -> nodeSequence++);

            Node from = new Node(firstNodeId, points.get(0));
            Node to = new Node(secondNodeId, points.get(points.size() - 1));
            Edge edge = new Edge(edgeSequence++, from, to, lineString.getDistance());
            storeEdge(edge, lineString);

            if (!isTrue(expandedWayKeyValues.get("oneway"))) {
                Edge edgeBack = new Edge(edgeSequence++, to, from, lineString.getDistance());
                storeEdge(edgeBack, lineString);
            }
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

    @Override
    protected void parse(Osmformat.HeaderBlock headerBlock) {
    }

    @Override
    public void complete() {
        System.out.printf("""
                ------------------------------------
                Completed loading OSM PBF
                Loaded %s nodes
                Loaded %s edges
                ------------------------------------
                %n""", nodeSequence, edgeSequence);
    }
}
