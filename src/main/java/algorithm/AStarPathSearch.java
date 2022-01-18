package algorithm;

import api.PathSearchAlgorithm;
import model.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarPathSearch implements PathSearchAlgorithm {

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

        while (!openSet.isEmpty()) {
            Node current = openSet.remove();

            if (current == destination) {
                return reconstructPath(cameFrom, destination);
            }

            openSet.remove(current);

            for (Edge connection : graph.getConnections(current)) {
                double tentativeGScore = gScore.getOrDefault(current, 0.0) + connection.getCost();
                Node neighbor = connection.getTo();
                if(!gScore.containsKey(neighbor)) {
                    cameFrom.put(neighbor, current);
                }
                if (tentativeGScore < gScore.getOrDefault(neighbor, 0.0)) {
                    // this path to neighbor is better than any previous one.
                    cameFrom.put(neighbor, current);
                }
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeGScore + heuristic(neighbor, destination));

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
            }
        }

        // no solution found!
        return null;
    }

    private Route reconstructPath(Map<Node, Node> cameFrom, Node current) {
        System.out.printf("solution found from %s%n\n", current);
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
