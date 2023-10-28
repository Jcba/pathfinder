package org.routing.web.controller;

import org.routing.libgeo.geojson.GJFeatureCollection;
import org.routing.libgeo.geojson.GJPoint;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.Node;
import org.routing.model.Route;
import org.routing.search.PathSearchAlgorithm;
import org.routing.storage.GeometryKeyReference;
import org.routing.storage.GeometryLookup;
import org.routing.storage.GeometryStore;
import org.routing.web.adapters.RouteToFeatureCollectionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("route")
public class RoutingController {

    private static final Logger log = LoggerFactory.getLogger(RoutingController.class);

    private final Graph graph;
    private final PathSearchAlgorithm pathSearchAlgorithm;
    private final GeometryStore<Edge> edgeGeometryStore;
    private final GeometryLookup geometryLookup;


    public RoutingController(Graph graph, PathSearchAlgorithm pathSearchAlgorithm, GeometryStore<Edge> edgeGeometryStore, GeometryLookup geometryLookup) {
        this.graph = graph;
        this.pathSearchAlgorithm = pathSearchAlgorithm;
        this.edgeGeometryStore = edgeGeometryStore;
        this.geometryLookup = geometryLookup;
    }

    @GetMapping("")
    public GJFeatureCollection route(@RequestParam("fromLat") Double fromLat,
                                     @RequestParam("fromLon") Double fromLon,
                                     @RequestParam("toLat") Double toLat,
                                     @RequestParam("toLon") Double toLon) {

        GeometryKeyReference closestFrom = geometryLookup.findClosest(new GJPoint(fromLat, fromLon));
        GeometryKeyReference closestTo = geometryLookup.findClosest(new GJPoint(toLat, toLon));

        log.info("closest from: {}", closestFrom);
        log.info("closest to: {}", closestTo);

        Edge fromEdge = graph.findEdge(closestFrom);
        Edge toEdge = graph.findEdge(closestTo);

        Node start = fromEdge.getFrom();
        Node destination = toEdge.getTo();

        Route route = pathSearchAlgorithm.route(start, destination);

        return new RouteToFeatureCollectionAdapter(edgeGeometryStore).apply(route);
    }
}
