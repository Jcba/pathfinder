package org.routing.storage;

import org.routing.model.Graph;

/**
 * The graph store is used to store and load graphs. Graphs will be stored on persistent storage such that they will be
 * available after application restarts and can be loaded quickly
 *
 * @param A graph
 */
public interface GraphStore<T extends Graph> {

    void save(Graph graph);

    T load(String name);
}
