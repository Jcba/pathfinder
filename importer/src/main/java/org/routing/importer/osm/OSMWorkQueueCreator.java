package org.routing.importer.osm;

import crosby.binary.Osmformat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OSMWorkQueueCreator extends AbstractOSMParser {

    private final EPSG84Grid grid;
    private Connection connection;

    public OSMWorkQueueCreator() {
        grid = EPSG84Grid.getGrid(1.0);
        openDatabase();
    }

    private void openDatabase() {
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists nodes");
            statement.executeUpdate("create table nodes (id long, lat float, lon float, grid_number int)");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void closeDatabase() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected void parseRelations(List<Osmformat.Relation> list) {
        //do nothing
    }

    @Override
    protected void parseDense(Osmformat.DenseNodes denseNodes) {
        long prevId = 0;
        double prevLat = 0;
        double prevLon = 0;
        for (int i = 0; i < denseNodes.getIdList().size(); i++) {
            long id = denseNodes.getId(i) + prevId;
            double lat = (parseLat(denseNodes.getLat(i)) + prevLat);
            double lon = (parseLon(denseNodes.getLon(i)) + prevLon);

            saveNode(id, lat, lon);

            prevId = id;
            prevLat = lat;
            prevLon = lon;
        }
    }

    private void saveNode(long nodeId, double lat, double lon) {
        int gridNumber = grid.getGridNumber(lat, lon);

        // save to database
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(
                    "insert into nodes (id, lat, lon, grid_number) values ('%s', '%s', '%s', '%s')",
                    nodeId, lat, lon, gridNumber));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void parseNodes(List<Osmformat.Node> list) {
        // do nothing
    }

    @Override
    protected void parseWays(List<Osmformat.Way> list) {
        for (Osmformat.Way way : list) {
            Map<String, List<String>> expandedWayKeyValues = getExpandedKeyVals(way.getKeysList(), way.getValsList());

            if (shouldParseWayType(expandedWayKeyValues)) {
                List<Long> refsList = way.getRefsList();
                List<Long> nodeIdList = new ArrayList<>();
                long prevNodeReference = 0;
                for (long ref : refsList) {
                    long nodeId = ref + prevNodeReference;
                    nodeIdList.add(nodeId);
                    prevNodeReference = nodeId;
                }
                saveEdge(nodeIdList);
            }
        }
    }

    private void saveEdge(List<Long> nodeIdList) {
        // save edge with nodeIds to correct grid block

    }

    @Override
    protected void parse(Osmformat.HeaderBlock headerBlock) {
    }

    @Override
    public void complete() {
        System.out.printf("""
                ------------------------------------------------
                Completed creating work queue files from OSM PBF
                ------------------------------------------------
                %n""");
        closeDatabase();
    }
}

