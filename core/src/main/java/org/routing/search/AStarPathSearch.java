package org.routing.search;

import org.routing.geometries.Point;
import org.routing.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarPathSearch implements PathSearchAlgorithm {

    private static final Logger log = LoggerFactory.getLogger(AStarPathSearch.class);

    private final Graph graph;

    public AStarPathSearch(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Route route(Node start, Node destination) {
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Double> fScore = new HashMap<>();

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(fScore::get));
        openSet.add(start);

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, destination));

        while (!openSet.isEmpty()) {
            Node current = openSet.remove();

            if (current.equals(destination)) {
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);

            for (Edge connection : graph.getConnections(current)) {
                double tentativeGScore = gScore.getOrDefault(current, 0.0) + connection.getCost();
                Node neighbor = connection.getTo();
                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    // this path to neighbor is better than any previous one.
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, destination));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        log.error("No solution found for route from {} to {}", start, destination);
        return null;
    }

    private Route reconstructPath(Map<Node, Node> cameFrom, Node current) {
        log.info("Solution found from {}", current);
        Route result = new Route();
        Node next = current;
        while (cameFrom.containsKey(next)) {
            result.addNodeOnRoute(next);
            next = cameFrom.get(next);
        }
        result.addNodeOnRoute(next);
        return result;
    }

    private double heuristic(Node a, Node b) {
        if (null == a || null == b || null == a.getCoordinate() || null == b.getCoordinate()) {
            // log error
            return 0.0;
        }
        Point pointA = a.getCoordinate();
        Point pointB = b.getCoordinate();
        return pointA.distance(pointB);
    }
}
