package org.routing.importer;

import org.routing.model.Graph;

import java.nio.file.Path;

public interface GraphImporter {

    Graph importFromFile(Path filePath, Graph graph);
}
