package org.routing.storage;

import org.routing.geometries.AbstractGeometry;
import org.routing.model.KeyProvider;

public interface GeometryStore<T extends KeyProvider> {

    void save(T id, AbstractGeometry<?> geometry);
}
