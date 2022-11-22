package org.routing.adapters;

import org.routing.geometries.Feature;
import org.routing.geometries.FeatureCollection;
import org.routing.geometries.LineString;
import org.routing.geometries.Point;
import org.routing.model.Node;
import org.routing.model.Route;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class RouteToFeatureCollectionAdapter implements Function<Route, FeatureCollection> {

    @Override
    public FeatureCollection apply(Route route) {
        List<Point> routePoints = route.getNodesOnRoute().stream()
                .map(Node::getCoordinate)
                .toList();

        LineString lineString = new LineString(routePoints);

        Feature feature = new Feature(UUID.randomUUID().toString(), lineString, List.of());

        List<Feature> features = List.of(feature);

        return new FeatureCollection(features);
    }
}
