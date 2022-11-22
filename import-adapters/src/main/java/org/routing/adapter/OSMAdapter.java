package org.routing.adapter;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat;
import crosby.binary.file.BlockInputStream;
import org.routing.geometries.LineString;
import org.routing.geometries.Point;
import org.routing.model.Edge;
import org.routing.model.MemoryGraph;
import org.routing.model.Node;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.READ;

public class OSMAdapter extends MemoryGraph {
    Map<Long, Point> nodeMap = new HashMap<>();

    public OSMAdapter(Path input) throws IOException {
        InputStream pathInputStream = Files.newInputStream(input, READ);
        try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream, new OSMWaysParser())) {
            blockInputStream.process();
        }
    }

    public class OSMWaysParser extends BinaryParser {

        private long loadedWays = 0;

        @Override
        protected void parseRelations(List<Osmformat.Relation> list) {
            //do nothing
        }

        @Override
        protected void parseDense(Osmformat.DenseNodes denseNodes) {
            long prevId = 0;
            float prevLat = 0;
            float prevLon = 0;
            for (int i = 0; i < denseNodes.getIdList().size(); i++) {
                long id = denseNodes.getId(i) + prevId;
                float lat = (float) (parseLat(denseNodes.getLat(i)) + prevLat);
                float lon = (float) (parseLon(denseNodes.getLon(i)) + prevLon);
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
                List<Integer> keys = way.getKeysList();
                for (int i = 0; i < keys.size(); i++) {
                    if (shouldParseWayType(getStringById(keys.get(i))) && shouldParseHighWayType(getStringById(way.getVals(i)))) {
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

                            Node from = new Node(points.get(0));
                            Node to = new Node(points.get(points.size() - 1));
                            Edge edge = new Edge(from, to, from.getCoordinate().distance(to.getCoordinate()), lineString);
                            addEdge(edge);
                            loadedWays++;
                        }
                    }
                }
            }
        }

        private boolean shouldParseWayType(String wayType) {
            return wayType.equals("highway");
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
}
