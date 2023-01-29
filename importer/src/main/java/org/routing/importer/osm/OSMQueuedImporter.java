package org.routing.importer.osm;

import crosby.binary.file.BlockInputStream;
import org.routing.importer.GraphImporter;
import org.routing.model.Graph;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.READ;

public class OSMQueuedImporter implements GraphImporter {

    @Override
    public Graph importFromFile(Path filePath, Graph graph) {
        createWorkQueue(filePath);
        return graph;
    }

    private void createWorkQueue(Path filePath) {
        try {
            InputStream pathInputStream = Files.newInputStream(filePath, READ);
            try (BlockInputStream blockInputStream = new BlockInputStream(pathInputStream,
                    new OSMWorkQueueCreator())) {
                blockInputStream.process();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}