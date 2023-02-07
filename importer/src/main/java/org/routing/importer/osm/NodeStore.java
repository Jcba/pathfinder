package org.routing.importer.osm;

import org.routing.model.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NodeStore {

    private Connection connection;

    public NodeStore() {
        createNewStore();
    }

    private void createNewStore() {
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:nodes.db");
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists nodes");
            statement.executeUpdate("create table nodes (id int primary key, lat float, lon float, degree int)");

            statement.close();

            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void open() {
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:nodes.db");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.commit();
                connection.close();
            }
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    public void save(long nodeId) {
        // save to database
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(
                    "insert or replace into nodes (id, lat, lon, degree) values ('%s', '%s', '%s', '%s')",
                    nodeId, 0, 0, 0));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(long nodeId, double lat, double lon) {
        // save to database
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(
                    "update nodes set lat='%s', lon='%s', degree='%s' where id='%s'",
                    lat, lon, 0, nodeId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Node> getAll(List<Long> nodeIdList) {
        List<Node> result = new ArrayList<>();

        StringBuilder nodeIdStringList = new StringBuilder();
        for (long nodeId : nodeIdList) {
            nodeIdStringList.append(nodeId);
            nodeIdStringList.append(",");
        }
        nodeIdStringList.deleteCharAt(nodeIdStringList.length() - 1);

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(
                    "select id, lat, lon, degree from nodes where id in (%s)", nodeIdStringList));
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                float lat = resultSet.getFloat("lat");
                float lon = resultSet.getFloat("lon");
                int degree = resultSet.getInt("degree");
                result.add(new Node(id, lat, lon, degree));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public record Node(long id, float lat, float lon, int degree) {
    }
}
