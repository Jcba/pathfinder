package org.routing.storage;

import org.routing.geometries.AbstractGeometry;
import org.routing.model.KeyProvider;

import java.util.List;

public interface GeometryLookup<T extends KeyProvider> {

    AbstractGeometry<?> findGeometry(T id);

    AbstractGeometry<?> findGeometries(List<T> ids);
}
