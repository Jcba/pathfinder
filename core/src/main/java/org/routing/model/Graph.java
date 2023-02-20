package org.routing.model;

import org.routing.storage.KeyProvider;

/**
 * Provides storage and retrieval Graph functions
 */
//FIXME: Split in two interfaces: Graph storage and Graph retrieval
public interface Graph {

    /**
     * Adds an Edge to this Graph.
     *
     * @param edge the Edge to be added
     */
    void addEdge(Edge edge);

    /**
     * Get all outgoing connections from this Node
     * <p>
     * Note that incoming Edges will <b>not</b> be returned
     *
     * @param node the input Node
     * @return all outgoing Edges going from this Node.
     */
    Edge[] getConnections(Node node);

    /**
     * Get the connection going from Node from to Node to
     *
     * @param from A Node
     * @param to   A Node
     * @return The Edge going from Node from to Node to, or null if no Edge can be found
     */
    Edge getConnection(Node from, Node to);

    /**
     * Get the Edge denominated by the KeyProvider
     *
     * @param key a KeyProvider.
     * @return The Edge: if an Edge can be found using the KeyProvider or null: otherwise
     */
    Edge findEdge(KeyProvider key);
}
