package org.routing.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private double distance;

    private List<Node> nodesOnRoute;

    private Graph graph;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<Node> getNodesOnRoute() {
        return nodesOnRoute;
    }

    public void addNodeOnRoute(Node point) {
        if (null == nodesOnRoute) {
            nodesOnRoute = new ArrayList<>();
        }
        this.nodesOnRoute.add(point);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

}
