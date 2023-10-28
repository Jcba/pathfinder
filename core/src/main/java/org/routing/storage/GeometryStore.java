package org.routing.storage;

import org.routing.libgeo.geojson.GJAbstractGeometry;

public interface GeometryStore<T extends KeyProvider> {

    /**
     * Stores a key-geometry pair
     *
     * @param id       the key to store
     * @param geometry the geometry belonging to the stored key
     */
    void save(T id, GJAbstractGeometry<?> geometry);

    /**
     * Find the geometry by keyProvider id
     *
     * @param id the keyprovider's id
     * @return the geometry belonging to the id, or null if no result can be found
     */
    GJAbstractGeometry<?> findById(T id);
}
