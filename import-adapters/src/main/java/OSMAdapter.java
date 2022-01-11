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
import java.util.List;

import static java.nio.file.StandardOpenOption.READ;

public class OSMAdapter extends Graph {

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
            //do nothing
        }

        @Override
        protected void parseNodes(List<Osmformat.Node> list) {
            //do nothing
        }

        @Override
        protected void parseWays(List<Osmformat.Way> list) {
            for (Osmformat.Way way : list) {
                Node from = new Node(new Point(0.0, 0.0));
                Node to = new Node(new Point(0.0, 0.0));
                Edge edge = new Edge(from, to, 0.0);
                addEdge(edge);
            }
        }

        @Override
        protected void parse(Osmformat.HeaderBlock headerBlock) {
            //do nothing
        }

        @Override
        public void complete() {
            //do nothing
        }
    }
}
