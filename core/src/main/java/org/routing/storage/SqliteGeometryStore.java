package org.routing.storage;

import org.routing.geometries.AbstractGeometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteGeometryStore<T extends KeyProvider> implements GeometryStore<T> {

    private static final Logger log = LoggerFactory.getLogger(SqliteGeometryStore.class);

    private final DatabaseConfiguration databaseConfiguration;

    public SqliteGeometryStore(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
        init();
    }

    public void init() {
        if (databaseConfiguration.createSchemaIfNotExists()) {
            executeSql("create schema if not exists routing");
            executeSql("create table if not exists routing.geometry_kv as (varchar(12) id, geometry geom)");
        }
    }

    @Override
    public void save(T key, AbstractGeometry<?> geometry) {
        key.getId();
        executeSql("insert into ");
    }

    @Override
    public AbstractGeometry<?> findById(T key) {
        long id = key.getId();
        return null;
    }

    private void executeSql(String sql) {
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
}
