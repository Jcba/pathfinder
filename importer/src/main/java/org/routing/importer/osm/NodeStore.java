package org.routing.importer.osm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

public class NodeStore {

    private static Integer totalNumberOfNodeStores = 0;

    private int nodeStoreNumber = 0;
    private static final Logger log = LoggerFactory.getLogger(NodeStore.class);
    private static final String JDBC_TEMPLATE_URL = "jdbc:sqlite:nodes_%d.db";
    private static final String SQL_INSERT_NODE_ID = "insert into nodes (id) values (?) on conflict(id) do update set degree=degree+1";
    private static final String SQL_UPDATE_NODE = "update nodes set lat=?, lon=? where id=?";

    private Connection connection;

    private final Map<StatementType, PreparedStatement> preparedStatementMap = new HashMap<>();

    enum StatementType {
        INSERT_NODE_IDS,
        UPDATE_NODE
    }

    public NodeStore() {
        createNewStore();
    }

    private void createNewStore() {
        try {
            // create a database connection

            connection = DriverManager.getConnection(createNewJdbcConnectionString(), dbSettings().toProperties());
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists nodes");
            statement.executeUpdate("create table nodes (id int primary key, lat float, lon float, degree int)");

            statement.close();

            connection.commit();

            setPreparedStatements();
        } catch (SQLException e) {
            log.error("Creating new NodeStore failed", e);
            throw new ImportFailureException(e);
        }
    }

    private String createNewJdbcConnectionString() {
        this.nodeStoreNumber = totalNumberOfNodeStores++;
        return getJdbcConnectionString();
    }

    private String getJdbcConnectionString() {
        return String.format(JDBC_TEMPLATE_URL, nodeStoreNumber);
    }


    public void open() {
        try {
            connection = DriverManager.getConnection(getJdbcConnectionString(), dbSettings().toProperties());
            connection.setAutoCommit(false);
            setPreparedStatements();
        } catch (SQLException e) {
            log.error("Opening existing NodeStore failed", e);
            throw new ImportFailureException(e);
        }
    }

    private void setPreparedStatements() {
        try {
            preparedStatementMap.put(StatementType.INSERT_NODE_IDS, connection.prepareStatement(SQL_INSERT_NODE_ID));
            preparedStatementMap.put(StatementType.UPDATE_NODE, connection.prepareStatement(SQL_UPDATE_NODE));
        } catch (SQLException e) {
            throw new ImportFailureException(e);
        }
    }

    private SQLiteConfig dbSettings() {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        // journaling & synchronous mode is not necessary since we will empty the database on start anyway
        config.setPragma(SQLiteConfig.Pragma.JOURNAL_MODE, "OFF");
        config.setPragma(SQLiteConfig.Pragma.SYNCHRONOUS, "OFF");
        // memory temp store will load indexes in memory
        config.setPragma(SQLiteConfig.Pragma.TEMP_STORE, "memory");
        // use Memory-Mapped-Files
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

    public void destroy() throws IOException {
        close();
        String[] splittedConnectionString = getJdbcConnectionString().split(":");
        String fileName = splittedConnectionString[splittedConnectionString.length - 1];
        Files.delete(Path.of(fileName));
    }

    public void saveId(long nodeId) {
        try {
            PreparedStatement statement = preparedStatementMap.get(StatementType.INSERT_NODE_IDS);
            statement.setLong(1, nodeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ImportFailureException(e);
        }
    }

    public void update(long nodeId, double lat, double lon) {
        try {
            PreparedStatement statement = preparedStatementMap.get(StatementType.UPDATE_NODE);
            statement.setDouble(1, lat);
            statement.setDouble(2, lon);
            statement.setDouble(3, nodeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ImportFailureException(e);
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
            throw new ImportFailureException(e);
        }

        for (Long nodeId : nodeIdList) {
            if (resultMap.containsKey(nodeId)) {
                result.add(resultMap.get(nodeId));
            }
        }

        return result;
    }

    public void saveAll(List<Node> nodeList) {
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
            throw new ImportFailureException(e);
        }

        for (Node node : nodeList) {
            if (existingNodeIds.contains(node.id)) {
                update(node.id, node.lat, node.lon);
            }
        }
    }

    public record Node(long id, float lat, float lon, int degree) {
    }
}
