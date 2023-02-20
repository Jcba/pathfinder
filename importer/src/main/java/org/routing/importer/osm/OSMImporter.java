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
    public void importFromFile(Path filePath, Graph graph) {
        NodeStore nodeStore = new NodeStore();
        storeUsedNodes(filePath, nodeStore);
        createGraph(filePath, graph, nodeStore);
    }

    private void storeUsedNodes(Path filePath, NodeStore nodeStore) {
        try {
            OSMNodeReader osmNodeReader = new OSMNodeReader(nodeStore);
            long fileSizeInBytes = Files.size(filePath);
            InputStream pathInputStream = Files.newInputStream(filePath, READ);
            CountingInputStream countingInputStream = new CountingInputStream(pathInputStream, fileSizeInBytes);

            try (BlockInputStream blockInputStream = new BlockInputStream(countingInputStream,
                    osmNodeReader)) {
                blockInputStream.process();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createGraph(Path filePath, Graph graph, NodeStore nodeStore) {
        try {
            long fileSizeInBytes = Files.size(filePath);
            InputStream pathInputStream = Files.newInputStream(filePath, READ);
            CountingInputStream countingInputStream = new CountingInputStream(pathInputStream, fileSizeInBytes);

            try (BlockInputStream blockInputStream = new BlockInputStream(countingInputStream,
                    new OSMGraphReader(nodeStore, graph, edgeGeometryStore))) {
                blockInputStream.process();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}