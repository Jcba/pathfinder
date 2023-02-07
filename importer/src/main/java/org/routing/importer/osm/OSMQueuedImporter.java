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

public class OSMQueuedImporter implements GraphImporter {

    private final GeometryStore<Edge> edgeGeometryStore;

    public OSMQueuedImporter(GeometryStore<Edge> edgeGeometryStore) {
        this.edgeGeometryStore = edgeGeometryStore;
    }

    @Override
    public Graph importFromFile(Path filePath, Graph graph) {
        NodeStore nodeStore = new NodeStore();
        storeUsedNodes(filePath, nodeStore);
        createGraph(filePath, graph, nodeStore);
        return graph;
    }

    private void storeUsedNodes(Path filePath, NodeStore nodeStore) {
        try {
            InputStream pathInputStream = Files.newInputStream(filePath, READ);
            try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream,
                    new OSMNodeReader(nodeStore))) {
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
            InputStream pathInputStream = Files.newInputStream(filePath, READ);
            try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream,
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