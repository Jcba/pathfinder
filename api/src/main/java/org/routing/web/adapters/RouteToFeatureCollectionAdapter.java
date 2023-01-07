package org.routing.web.adapters;

import org.routing.geometries.AbstractGeometry;
import org.routing.geometries.Feature;
import org.routing.geometries.FeatureCollection;
import org.routing.model.Edge;
import org.routing.model.Node;
import org.routing.model.Route;
import org.routing.storage.GeometryStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

public class RouteToFeatureCollectionAdapter implements Function<Route, FeatureCollection> {

    private final GeometryStore<Edge> geometryLookup;

    public RouteToFeatureCollectionAdapter(GeometryStore<Edge> geometryLookup) {
        this.geometryLookup = geometryLookup;
    }

    @Override
    public FeatureCollection apply(Route route) {
        List<Edge> edges = new ArrayList<>();
        List<Feature> features = new ArrayList<>();

        if (null == route) {
            return new FeatureCollection(List.of());
        }

        List<Node> nodesOnRoute = route.getNodesOnRoute();
        for (int i = nodesOnRoute.size() - 1; i >= 1; i--) {
            edges.add(route.getGraph().getConnection(nodesOnRoute.get(i), nodesOnRoute.get(i - 1)));
        }

        for (Edge edge : edges) {
            AbstractGeometry<?> edgeGeometry = geometryLookup.findById(edge);
            Feature feature = new Feature(UUID.randomUUID().toString(), edgeGeometry, new Properties());
            features.add(feature);
        }

        return new FeatureCollection(features);
    }
}
