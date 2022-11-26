package org.routing.model;

import java.util.UUID;

public interface KeyProvider {

    /**
     * Represents an object type name
     * The object type name should be globally unique
     *
     * @return the object type name
     */
    String getType();

    /**
     * Returns a globally unique id
     *
     * @return a globally unique id
     */
    UUID getId();
}
