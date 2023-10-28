package org.routing.web.adapters;

import org.routing.libgeo.geojson.GJAbstractGeometry;
import org.routing.libgeo.geojson.GJFeature;
import org.routing.libgeo.geojson.GJFeatureCollection;
import org.routing.model.Edge;
import org.routing.model.Node;
import org.routing.model.Route;
import org.routing.storage.GeometryStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

public class RouteToFeatureCollectionAdapter implements Function<Route, GJFeatureCollection> {

    private final GeometryStore<Edge> geometryLookup;

    public RouteToFeatureCollectionAdapter(GeometryStore<Edge> geometryLookup) {
        this.geometryLookup = geometryLookup;
    }

    @Override
    public GJFeatureCollection apply(Route route) {
        List<Edge> edges = new ArrayList<>();
        List<GJFeature> GJFeatures = new ArrayList<>();

        if (null == route) {
            return new GJFeatureCollection(List.of());
        }

        List<Node> nodesOnRoute = route.getNodesOnRoute();
        for (int i = nodesOnRoute.size() - 1; i >= 1; i--) {
            edges.add(route.getGraph().getConnection(nodesOnRoute.get(i), nodesOnRoute.get(i - 1)));
        }

        for (Edge edge : edges) {
            GJAbstractGeometry<?> edgeGeometry = geometryLookup.findById(edge);
            GJFeature GJFeature = new GJFeature(UUID.randomUUID().toString(), edgeGeometry, new Properties());
            GJFeatures.add(GJFeature);
        }

        return new GJFeatureCollection(GJFeatures);
    }
}
