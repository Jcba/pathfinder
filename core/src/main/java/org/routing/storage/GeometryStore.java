package org.routing.storage;

import org.routing.geometries.AbstractGeometry;

public interface GeometryStore<T extends KeyProvider> {

    void save(T id, AbstractGeometry<?> geometry);

    AbstractGeometry<?> findById(T id);
}
