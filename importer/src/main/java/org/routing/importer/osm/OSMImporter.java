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

import static java.nio.file.StandardOpenOption.READ;

public class OSMImporter implements GraphImporter {

    private final GeometryStore<Edge> edgeGeometryStore;

    public OSMImporter(GeometryStore<Edge> edgeGeometryStore) {
        this.edgeGeometryStore = edgeGeometryStore;
    }

    @Override
    public Graph importFromFile(Path filePath, Graph graph) {
        InputStream pathInputStream = null;

        try {
            pathInputStream = Files.newInputStream(filePath, READ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream, new OSMParser(graph, edgeGeometryStore))) {
            blockInputStream.process();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return graph;
    }
}
