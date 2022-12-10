package org.routing.storage;

import org.routing.geometries.AbstractGeometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class SqliteGeometryStore<T extends KeyProvider> implements GeometryStore<T> {

    private static final Logger log = LoggerFactory.getLogger(SqliteGeometryStore.class);

    private final DatabaseConfiguration databaseConfiguration;

    public SqliteGeometryStore(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
        init();
    }

    private void init() {
        if(databaseConfiguration.dropTableIfExists()) {
            executeUpdate("drop table if exists geometry_kv;");
        }
        if (databaseConfiguration.createSchemaIfNotExists()) {
            executeUpdate("create table if not exists geometry_kv (key_id integer primary key, geom blob);");
        }
    }

    @Override
    public void save(T key, AbstractGeometry<?> geometry) {
        key.getId();
        executeUpdate(String.format("insert into geometry_kv (key_id, geom) values (%s, %s);", key.getId(), geometry));
    }

    @Override
    public AbstractGeometry<?> findById(T key) {
        long id = key.getId();
        try (ResultSet resultSet = executeQuery(String.format("select geom from geometry_kv where key_id = '%s'", key.getId()))) {
            return resultSet.getObject("geom", AbstractGeometry.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(String sql) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection(databaseConfiguration.jdbcUrl());
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(databaseConfiguration.queryTimeoutSeconds());
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                log.error(e.getMessage(), e);
            }
        }
    }

    private ResultSet executeQuery(String sql) {
        Connection connection = null;
        ResultSet result = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection(databaseConfiguration.jdbcUrl());
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(databaseConfiguration.queryTimeoutSeconds());
            result = statement.executeQuery(sql);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }
}
