package org.routing.importer.osm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.*;

public class NodeStore {

    private final static Logger log = LoggerFactory.getLogger(NodeStore.class);

    private Connection connection;

    public NodeStore() {
        createNewStore();
    }

    private void createNewStore() {
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:nodes.db", dbSettings().toProperties());
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists nodes");
            statement.executeUpdate("create table nodes (id int primary key, lat float, lon float, degree int)");

            statement.close();

            connection.commit();
        } catch (SQLException e) {
            log.error("Creating new NodeStore failed", e);
        }
    }

    public void open() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:nodes.db", dbSettings().toProperties());
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("Opening existing NodeStore failed", e);
        }
    }

    private SQLiteConfig dbSettings() {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.setPragma(SQLiteConfig.Pragma.JOURNAL_MODE, "OFF");
        config.setPragma(SQLiteConfig.Pragma.SYNCHRONOUS, "OFF");
        config.setPragma(SQLiteConfig.Pragma.TEMP_STORE, "memory");
        config.setPragma(SQLiteConfig.Pragma.MMAP_SIZE, "3000000000");
        return config;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.commit();
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Closing NodeStore failed", e);
        }
    }

    public void save(long nodeId) {
        // save to database
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(
                    "insert into nodes (id, lat, lon, degree) values ('%s', '%s', '%s', '%s') on conflict(id) do update set degree=degree+1",
                    nodeId, 0, 0, 0));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(long nodeId, double lat, double lon) {
        // save to database
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("update nodes set lat='%s', lon='%s' where id='%s'",
                    lat, lon, nodeId);
            statement.executeUpdate(sql);
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

        Map<Long, Node> resultMap = new HashMap<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(
                    "select id, lat, lon, degree from nodes where id in (%s)", nodeIdStringList));
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                float lat = resultSet.getFloat("lat");
                float lon = resultSet.getFloat("lon");
                int degree = resultSet.getInt("degree");
                resultMap.put(id, new Node(id, lat, lon, degree));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Long nodeId : nodeIdList) {
            if (resultMap.containsKey(nodeId)) {
                result.add(resultMap.get(nodeId));
            }
        }

        return result;
    }

    public void save(List<Node> nodeList) {
        StringBuilder nodeIdStringList = new StringBuilder();
        for (Node node : nodeList) {
            nodeIdStringList.append(node.id);
            nodeIdStringList.append(",");
        }
        nodeIdStringList.deleteCharAt(nodeIdStringList.length() - 1);

        Set<Long> existingNodeIds = new HashSet<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(
                    "select id from nodes where id in (%s)", nodeIdStringList));

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                existingNodeIds.add(id);
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Node node : nodeList) {
            if (existingNodeIds.contains(node.id)) {
                save(node.id, node.lat, node.lon);
            }
        }
    }

    public record Node(long id, float lat, float lon, int degree) {
    }
}