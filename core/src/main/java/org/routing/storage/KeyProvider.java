package org.routing.storage;

public interface KeyProvider {

    /**
     * Represents an object type name
     * The object type name should be globally unique
     *
     * @return the object type name
     */
    String getType();

    /**
     * Get the id for this element.
     * Each element of the same key type should have its own unique id.
     *
     * @return the id of this element
     */
    long getId();
}
