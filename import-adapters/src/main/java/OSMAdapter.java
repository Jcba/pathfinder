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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.READ;

public class OSMAdapter extends Graph {

    public OSMAdapter(Path input) throws IOException {
        InputStream pathInputStream = Files.newInputStream(input, READ);
        try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream, new OSMWaysParser())) {
            blockInputStream.process();
        }
    }

    public class OSMWaysParser extends BinaryParser {
        int counter = 0;
        Map<Long, Point> nodeMap = new HashMap<>();

        @Override
        protected void parseRelations(List<Osmformat.Relation> list) {
            //do nothing
        }

        @Override
        protected void parseDense(Osmformat.DenseNodes denseNodes) {
            int idNr = 0;
            for (int i = 0; i < denseNodes.getKeysValsList().size(); i++) {
                int kv = denseNodes.getKeysValsList().get(i);
                if (kv == 0) {
                    // delimiter, do nothing
                    idNr += 1;
                } else {
                    i += 1;
                    int valueInt = denseNodes.getKeysValsList().get(i);
                    String key = getStringById(kv);
                    String value = getStringById(valueInt);
                    nodeMap.put(denseNodes.getId(idNr), new Point(denseNodes.getLat(idNr), denseNodes.getLon(idNr)));
                }
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
                    if (getStringById(keys.get(i)).equals("highway")) {
                        System.out.println("way: " + getStringById(way.getVals(i)));

                        List<Long> refsList = way.getRefsList();
                        List<Point> points = refsList.stream().map(nodeMap::get).collect(Collectors.toList());

                        if (!refsList.isEmpty() && points.size() >= 2) {
                            Node from = new Node(new Point(0.0, 0.0));
                            Node to = new Node(new Point(0.0, 0.0));
                            Edge edge = new Edge(from, to, 0.0);
                            addEdge(edge);
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
            //do nothing
        }
    }
}
