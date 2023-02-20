package org.routing.importer;

import org.routing.model.Graph;

import java.nio.file.Path;

/**
 * Interface for all graph importers
 * <p>
 * Implementations of this interface can also do conversions of input files to read them into a Graph
 */
public interface GraphImporter {

    /**
     * Imports a graph from an input file
     *
     * @param filePath the file or directory to read, containing a graph
     * @param graph    an instance of Graph, where the imported routing graph will be stored into
     */
    void importFromFile(Path filePath, Graph graph);
}
