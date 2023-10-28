package org.routing.storage;

import org.routing.libgeo.geojson.GJAbstractGeometry;

public interface GeometryLookup {

    /**
     * Find the closest geometry of type T for the given input geometry
     *
     * @param geometry the input geometry
     * @return the closest geometry of type T
     */
    GeometryKeyReference findClosest(GJAbstractGeometry<?> geometry);

}
