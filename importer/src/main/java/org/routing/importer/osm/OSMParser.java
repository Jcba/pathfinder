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
    private final Graph graph;
    private final GeometryStore<Edge> edgeGeometryStore;

    private long loadedWays = 0;
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
            Map<String, String> expandedWayKeyValues = getExpandedKeyVals(way.getKeysList(), way.getValsList());

            if (shouldParseWayType(expandedWayKeyValues)) {
                List<Long> refsList = way.getRefsList();
                List<Point> points = new ArrayList<>();
                long prevNodeReference = 0;
                for (long ref : refsList) {
                    Point node = nodeMap.get(ref + prevNodeReference);
                    points.add(node);
                    prevNodeReference = ref + prevNodeReference;
                }

                if (!refsList.isEmpty() && points.size() >= 2) {
                    LineString lineString = new LineString(points);

                    long firstNodeId = pointNodeIdMap.computeIfAbsent(points.get(0), v -> nodeSequence++);
                    long secondNodeId = pointNodeIdMap.computeIfAbsent(points.get(points.size() - 1), v -> nodeSequence++);

                    Node from = new Node(firstNodeId, points.get(0));
                    Node to = new Node(secondNodeId, points.get(points.size() - 1));
                    Edge edge = new Edge(edgeSequence++, from, to, lineString.getDistance());
                    storeEdge(edge, lineString);

//                    if (isTrue(expandedWayKeyValues.get("oneway"))) {
                        Edge edgeBack = new Edge(edgeSequence++, to, from, lineString.getDistance());
                        storeEdge(edgeBack, lineString);
//                    }

                    loadedWays++;
                }
            }
        }
    }

    private boolean isTrue(String value) {
        if (null == value) {
            return false;
        }
        return value.equals("yes") ||
                value.equals("true") ||
                value.equals("1");
    }

    private Map<String, String> getExpandedKeyVals(List<Integer> keysList, List<Integer> valsList) {
        Map<String, String> result = new HashMap<>(keysList.size());
        for (int i = 0; i < keysList.size(); i++) {
            result.put(getStringById(keysList.get(i)), getStringById(valsList.get(i)));
        }
        return result;
    }

    private boolean shouldParseWayType(Map<String, String> expandedWayKeyVals) {
        return expandedWayKeyVals.containsKey("highway") && shouldParseHighWayType(expandedWayKeyVals.get("highway"));
    }

    private boolean shouldParseHighWayType(String highwayType) {
        return highwayType.equals("motorway") ||
                highwayType.equals("trunk") ||
                highwayType.equals("primary") ||
                highwayType.equals("secondary") ||
                highwayType.equals("tertiary") ||
                highwayType.equals("unclassified") ||
                highwayType.equals("residential") ||
                highwayType.equals("motorway_link") ||
                highwayType.equals("trunk_link") ||
                highwayType.equals("primary_link") ||
                highwayType.equals("secondary_link") ||
                highwayType.equals("tertiary_link");
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
                %n""", nodeMap.size(), loadedWays);
    }
}
