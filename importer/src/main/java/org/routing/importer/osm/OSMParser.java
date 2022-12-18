package org.routing.importer.osm;

import crosby.binary.BinaryParser;
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

public class OSMParser extends BinaryParser {

    private final Map<Point, Long> pointNodeIdMap = new HashMap<>();
    private final Map<Long, Point> nodeMap = new HashMap<>();
    private final Map<Long, Short> nodeDegreeMap = new HashMap<>();
    private final Graph graph;
    private final GeometryStore<Edge> edgeGeometryStore;

    private long nodeSequence = 0;
    private long edgeSequence = 0;

    public OSMParser(Graph graph, GeometryStore<Edge> edgeGeometryStore) {
        this.graph = graph;
        this.edgeGeometryStore = edgeGeometryStore;
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
                long prevNodeReference = 0;
                for (long ref : refsList) {
                    long nodeId = ref + prevNodeReference;
                    if (nodeDegreeMap.containsKey(nodeId)) {
                        nodeDegreeMap.put(nodeId, (short) (nodeDegreeMap.get(nodeId) + 1));
                    } else {
                        nodeDegreeMap.put(nodeId, (short) 1);
                    }
                    prevNodeReference = nodeId;
                }
            }
        }

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
                    if (nodeDegreeMap.get(nodeId) > 1) {
                        addEdge(points, expandedWayKeyValues);
                        points = new ArrayList<>();
                        points.add(node);
                    }
                    prevNodeReference = nodeId;
                }
                addEdge(points, expandedWayKeyValues);
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

    private Map<String, List<String>> getExpandedKeyVals(List<Integer> keysList, List<Integer> valsList) {
        Map<String, List<String>> result = new HashMap<>(keysList.size());
        for (int i = 0; i < keysList.size(); i++) {
            String key = getStringById(keysList.get(i));
            String value = getStringById(valsList.get(i));
            if (result.containsKey(key)) {
                result.get(key).add(value);
            } else {
                result.put(key, List.of(value));
            }
        }

        return result;
    }

    private boolean shouldParseWayType(Map<String, List<String>> expandedWayKeyVals) {
        return expandedWayKeyVals.containsKey("highway") && shouldParseHighWayType(expandedWayKeyVals.get("highway"));
    }

    private boolean shouldParseHighWayType(List<String> highwayTypes) {
        return highwayTypes.contains("motorway") ||
                highwayTypes.contains("trunk") ||
                highwayTypes.contains("primary") ||
                highwayTypes.contains("secondary") ||
                highwayTypes.contains("tertiary") ||
                highwayTypes.contains("unclassified") ||
                highwayTypes.contains("residential") ||
                highwayTypes.contains("motorway_link") ||
                highwayTypes.contains("trunk_link") ||
                highwayTypes.contains("primary_link") ||
                highwayTypes.contains("secondary_link") ||
                highwayTypes.contains("tertiary_link");
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
