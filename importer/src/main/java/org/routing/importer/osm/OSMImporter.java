package org.routing.importer.osm;

import crosby.binary.file.BlockInputStream;
import org.routing.importer.GraphImporter;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.storage.GeometryStore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static java.nio.file.StandardOpenOption.READ;

public class OSMImporter implements GraphImporter {

    private final GeometryStore<Edge> edgeGeometryStore;

    public OSMImporter(GeometryStore<Edge> edgeGeometryStore) {
        this.edgeGeometryStore = edgeGeometryStore;
    }

    @Override
    public Graph importFromFile(Path filePath, Graph graph) {
        Map<Long, Short> nodeDegreeMap = readNodeDegreeFromFile(filePath);
        return readGraphFromFile(filePath, graph, nodeDegreeMap);
    }

    private Graph readGraphFromFile(Path filePath, Graph graph, Map<Long, Short> nodeDegreeMap) {
        InputStream pathInputStream;

        try {
            pathInputStream = Files.newInputStream(filePath, READ);
            try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream,
                    new OSMGraphParser(graph, edgeGeometryStore, nodeDegreeMap))) {
                blockInputStream.process();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return graph;
    }

    private Map<Long, Short> readNodeDegreeFromFile(Path filePath) {
        InputStream pathInputStream;

        OSMNodeDegreeParser osmNodeDegreeParser = new OSMNodeDegreeParser();

        try {
            pathInputStream = Files.newInputStream(filePath, READ);
            try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream, osmNodeDegreeParser)) {
                blockInputStream.process();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return osmNodeDegreeParser.getNodeDegreeMap();
    }
}
