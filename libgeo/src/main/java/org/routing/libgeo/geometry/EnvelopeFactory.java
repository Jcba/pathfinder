package org.routing.libgeo.geometry;

public class EnvelopeFactory {

    public static Envelope of(Geometry geometry) {
        return switch (geometry) {
            case LineString lineString -> calculateEnvelope(lineString);
            case Point point -> calculateEnvelope(point);
        };
    }

    private static Envelope calculateEnvelope(Point point) {
        return Envelope.builder()
                .min(point.lat(), point.lon())
                .max(point.lat(), point.lon())
                .build();
    }

    private static Envelope calculateEnvelope(LineString lineString) {
        double minLat = Integer.MAX_VALUE;
        double minLon = Integer.MAX_VALUE;
        double maxLat = Integer.MIN_VALUE;
        double maxLon = Integer.MIN_VALUE;

        for (Point point : lineString.points()) {
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
}
