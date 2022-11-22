package org.routing.adapters;

import org.routing.geometries.Feature;
import org.routing.geometries.FeatureCollection;
import org.routing.geometries.LineString;
import org.routing.geometries.Point;
import org.routing.model.Edge;
import org.routing.model.Node;
import org.routing.model.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class RouteToFeatureCollectionAdapter implements Function<Route, FeatureCollection> {

    @Override
    public FeatureCollection apply(Route route) {
        List<Edge> edges = new ArrayList<>();
        List<Node> nodesOnRoute = route.getNodesOnRoute();
        for (int i = 0; i < nodesOnRoute.size() - 1; i++) {
            edges.add(route.getGraph().getConnection(nodesOnRoute.get(i+1), nodesOnRoute.get(i)));
        }

        List<Point> routePoints = new ArrayList<>();
        for (Edge edge : edges) {
            routePoints.addAll(edge.getGeometry().getGeometry());
        }

        LineString lineString = new LineString(routePoints);

        Feature feature = new Feature(UUID.randomUUID().toString(), lineString, List.of());

        List<Feature> features = List.of(feature);

        return new FeatureCollection(features);
    }
}
