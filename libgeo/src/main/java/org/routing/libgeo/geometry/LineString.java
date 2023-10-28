package org.routing.libgeo.geometry;

import java.util.List;
import java.util.Objects;

public final class LineString implements Geometry {
    private final List<Point> points;
    private final Envelope envelope;

    public LineString(List<Point> points) {
        this.points = points;
        envelope = calculateEnvelope();
    }

    public double getDistance() {
        double distance = 0;
        if (points.size() < 2) {
            return distance;
        }
        for (int i = 0; i < points.size() - 1; i++) {
            distance += points.get(i).distance(points.get(i + 1));
        }
        return distance;
    }

    @Override
    public Envelope getEnvelope() {
        return envelope;
    }

    private Envelope calculateEnvelope() {
        double minLat = Integer.MAX_VALUE;
        double minLon = Integer.MAX_VALUE;
        double maxLat = Integer.MIN_VALUE;
        double maxLon = Integer.MIN_VALUE;

        for (Point point : points) {
            if (point.lat() < minLat) {
                minLat = point.lat();
            }
            if (point.lat() > maxLat) {
                maxLat = point.lat();
            }
            if (point.lon() < minLon) {
                minLon = point.lon();
            }
            if (point.lon() > maxLon) {
                maxLon = point.lon();
            }
        }

        return Envelope.builder()
                .min(minLat, minLon)
                .max(maxLat, maxLon)
                .build();
    }

    public List<Point> points() {
        return points;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LineString) obj;
        return Objects.equals(this.points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        return "LineString[" +
                "points=" + points + ']';
    }

}
