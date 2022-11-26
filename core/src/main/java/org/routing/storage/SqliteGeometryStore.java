package org.routing.storage;

import org.routing.geometries.AbstractGeometry;
import org.routing.model.Edge;
import org.routing.model.KeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqliteGeometryStore<T extends KeyProvider>
        implements GeometryStore<T>, GeometryLookup<T>, EdgeLookup {

    private static final Logger log = LoggerFactory.getLogger(SqliteGeometryStore.class);

    private final DatabaseConfiguration databaseConfiguration;

    public SqliteGeometryStore(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
        init();
    }

    public void init() {
        if (databaseConfiguration.createSchemaIfNotExists()) {
            executeSql("create schema if not exists routing");
            executeSql("create table if not exists routing.geometry_kv as (varchar(8) id, geometry geom)");
        }
    }

    @Override
    public void save(T id, AbstractGeometry<?> geometry) {
        id.getId();
        executeSql("insert into ");
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

    @Override
    public AbstractGeometry<?> findGeometry(T id) {
        return null;
    }

    @Override
    public AbstractGeometry<?> findGeometries(List<T> ids) {
        return null;
    }

    @Override
    public Edge findClosest(AbstractGeometry<?> geometry) {
        // find closest
        return null;
    }
}
