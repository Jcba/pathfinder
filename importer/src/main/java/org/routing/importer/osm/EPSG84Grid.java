package org.routing.importer.osm;

public class EPSG84Grid {

    private static final double MIN_LON = -180.0;
    private static final double MAX_LON = 180.0;
    private static final double MIN_LAT = -90.0;
    private static final double MAX_LAT = 90.0;

    private double resolution;

    private EPSG84Grid() {
        // do not instantiate directly
    }

    public static EPSG84Grid getGrid(double resolution) {
        EPSG84Grid result = new EPSG84Grid();
        result.resolution = resolution;
        return result;
    }

    public int getGridNumber(double lat, double lon) {
        double latSpread = MAX_LAT + Math.abs(MIN_LAT);

        double numberOfLatCells = latSpread / resolution;

        double latOffset = lat + MAX_LAT;
        double lonOffset = lon + MAX_LON;

        return (int) ((latOffset / resolution) + ((lonOffset / resolution) * numberOfLatCells));
    }

}
