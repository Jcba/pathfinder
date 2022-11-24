package org.routing.importer;

import java.nio.file.Path;

public interface GraphImporter {

    void importFromFile(Path filePath);
}
