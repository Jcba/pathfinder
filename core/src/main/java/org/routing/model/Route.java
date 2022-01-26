package org.routing.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private double distance;

    private List<Node> nodesOnRoute;

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
}
