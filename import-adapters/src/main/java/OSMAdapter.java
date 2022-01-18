import crosby.binary.BinaryParser;
import crosby.binary.Osmformat;
import crosby.binary.file.BlockInputStream;
import model.Edge;
import model.Graph;
import model.Node;
import model.Point;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.READ;

public class OSMAdapter extends Graph {
    Map<Long, Node> nodeMap = new HashMap<>();

    public OSMAdapter(Path input) throws IOException {
        InputStream pathInputStream = Files.newInputStream(input, READ);
        try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream, new OSMWaysParser())) {
            blockInputStream.process();
        }
    }

    public class OSMWaysParser extends BinaryParser {

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
                double lat = parseLat(denseNodes.getLat(i)) + prevLat;
                double lon = parseLon(denseNodes.getLon(i)) + prevLon;
                if (!nodeMap.containsKey(id)) {
                    nodeMap.put(id, new Node(new Point(lat, lon)));
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
                for (Integer key : keys) {
                    if (getStringById(key).equals("highway")) {
                        List<Long> refsList = way.getRefsList();
                        List<Node> points = new ArrayList<>();
                        long prevNodeReference = 0;
                        for (long ref : refsList) {
                            Node node = nodeMap.get(ref + prevNodeReference);
                            points.add(node);
                            prevNodeReference = ref + prevNodeReference;
                        }

                        if (!refsList.isEmpty() && points.size() >= 2) {
                            for (int p = 0; p < points.size() - 1; p++) {
                                Node from = points.get(p);
                                Node to = points.get(p + 1);
                                Edge edge = new Edge(from, to, from.getCoordinate().distance(to.getCoordinate()));
                                addEdge(edge);
                            }
                        }
                    }
                }
            }
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
                    ------------------------------------
                    %n""", nodeMap.size());
        }
    }
}
