package org.routing.libgeo.math;

import org.routing.libgeo.geometry.Geometry;
import org.routing.libgeo.geometry.LineString;
import org.routing.libgeo.geometry.Point;

public class Distance {

    public static double between(Geometry geom1, Geometry geom2) {
        return switch (geom1) {
            case LineString lineString -> calculateDistance(lineString, geom2);
            case Point point -> calculateDistance(point, geom2);
        };
    }

    private static double calculateDistance(Point point, Geometry geom2) {
        return switch (geom2) {
            case LineString lineString -> calculateDistance(point, lineString);
            case Point point2 -> calculateDistance(point, point2);
        };
    }

    private static double calculateDistance(LineString lineString, Geometry geom2) {
        return switch (geom2) {
            case LineString lineString2 -> calculateDistance(lineString, lineString2);
            case Point point -> calculateDistance(point, lineString);
        };
    }

    private static double calculateDistance(LineString lineString1, LineString lineString2) {
        double shortestDistance = Double.MAX_VALUE;
        for (Point point1 : lineString1.points()) {
            for (Point point2 : lineString2.points()) {
                if (point1.distance(point2) < shortestDistance) {
                    shortestDistance = point1.distance(point2);
                }
            }
        }
        return shortestDistance;
    }

    private static double calculateDistance(Point point, LineString lineString) {
        double shortestDistance = Double.MAX_VALUE;
        for (Point point1 : lineString.points()) {
            if (point1.distance(point) < shortestDistance) {
                shortestDistance = point1.distance(point);
            }
        }
        return shortestDistance;
    }

    private static double calculateDistance(Point point1, Point point2) {
        return point1.distance(point2);
    }
}
