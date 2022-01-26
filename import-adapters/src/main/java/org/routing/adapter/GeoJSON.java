package org.routing.adapter;

import org.routing.model.Point;

import java.util.List;

public class GeoJSON {

    public static String asLineString(List<Point> collect) {
        StringBuilder builder = new StringBuilder();
        builder.append("""
                {
                  "type": "FeatureCollection",
                  "features": [
                  """);
            builder.append(
                    """
                            {
                              "type": "Feature",
                              "properties": {},
                              "geometry": {
                                "type": "LineString",
                                "coordinates": [
                                """);
        for (int i = 0; i < collect.size(); i++) {
            Point point = collect.get(i);
            builder.append(String.format("[%s, %s]", point.getY(), point.getX()));
            if(i != collect.size()-1) {
                builder.append(",");
            }
        }
        builder.append("]}}]}");
        return builder.toString();
    }
}
