package org.routing.web.adapters;

import org.routing.geometries.Feature;
import org.routing.geometries.FeatureCollection;
import org.routing.geometries.LineString;
import org.routing.geometries.Point;
import org.routing.model.Edge;
import org.routing.model.Node;
import org.routing.model.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

public class RouteToFeatureCollectionAdapter implements Function<Route, FeatureCollection> {

    @Override
    public FeatureCollection apply(Route route) {
        List<Edge> edges = new ArrayList<>();

        if (null == route) {
            return new FeatureCollection(List.of());
        }

        List<Node> nodesOnRoute = route.getNodesOnRoute();
        for (int i = nodesOnRoute.size() - 1; i >= 1; i--) {
            edges.add(route.getGraph().getConnection(nodesOnRoute.get(i), nodesOnRoute.get(i - 1)));
        }

        List<Point> routePoints = new ArrayList<>();
        for (Edge edge : edges) {
//            routePoints.addAll(edge.getGeometry().points());
        }

        LineString lineString = new LineString(routePoints);

        Feature feature = new Feature(UUID.randomUUID().toString(), lineString, new Properties());

        List<Feature> features = List.of(feature);

        return new FeatureCollection(features);
    }
}
