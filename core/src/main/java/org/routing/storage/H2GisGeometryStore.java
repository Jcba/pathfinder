package org.routing.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.routing.geometries.AbstractGeometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class H2GisGeometryStore<T extends KeyProvider> implements GeometryStore<T>, GeometryLookup {

    private static final Logger log = LoggerFactory.getLogger(H2GisGeometryStore.class);

    private final DatabaseConfiguration databaseConfiguration;

    private final ObjectMapper objectMapper;

    private Connection dbConnection;

    public H2GisGeometryStore(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
        objectMapper = new ObjectMapper();
        init();
    }

    private void init() {
        enableGISDBExtension();

        if (databaseConfiguration.dropTableIfExists()) {
            executeUpdate("drop table if exists geometry_kv;");
        }
        if (databaseConfiguration.createSchemaIfNotExists()) {
            executeUpdate("create table if not exists geometry_kv (key_id bigint primary key, geom geometry);");
        }

        createSpatialIndex();
    }

    private void enableGISDBExtension() {
        executeUpdate("CREATE ALIAS IF NOT EXISTS H2GIS_SPATIAL FOR \"org.h2gis.functions.factory.H2GISFunctions.load\"");
        executeUpdate("CALL H2GIS_SPATIAL();");
    }

    private void createSpatialIndex() {
        executeUpdate("CREATE SPATIAL INDEX geometry_kv_geom_idx ON geometry_kv(geom);");
    }

    public void onExit() {
        if (null != dbConnection) {
            try {
                if (!dbConnection.isClosed()) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void save(T id, AbstractGeometry<?> geometry) {
        try {
            executeUpdate(String.format("insert into geometry_kv (key_id, geom) values ('%s', ST_GeomFromGeoJson('%s'));",
                    id.getId(), objectMapper.writeValueAsString(geometry)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AbstractGeometry<?> findById(T id) {
        try (ResultSet resultSet = executeQuery(String.format("select ST_AsGeoJson(geom) geojson from geometry_kv where key_id = '%s'", id.getId()))) {
            resultSet.next();
            String geojson = resultSet.getString("geojson");
            return objectMapper.readValue(geojson, AbstractGeometry.class);
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GeometryKeyReference findClosest(AbstractGeometry<?> geometry) {
        try (ResultSet resultSet = executeQuery(String.format("""
                        select key_id, st_distance(geom, ST_GeomFromGeoJson('%s')) as distance
                        from geometry_kv
                        order by distance limit 1
                        """,
                objectMapper.writeValueAsString(geometry)))) {
            resultSet.next();
            long keyId = resultSet.getLong("key_id");
            return new GeometryKeyReference("edge", keyId);
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(String sql) {
        try {
            // create a database connection
            Connection connection = getDbConnection();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(databaseConfiguration.queryTimeoutSeconds());
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private ResultSet executeQuery(String sql) {
        ResultSet result = null;
        try {
            // create a database connection
            Connection connection = getDbConnection();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(databaseConfiguration.queryTimeoutSeconds());
            result = statement.executeQuery(sql);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private Connection getDbConnection() {
        try {
            if (null == dbConnection) {
                dbConnection = DriverManager.getConnection(databaseConfiguration.jdbcUrl());
            }
            if (dbConnection.isClosed()) {
                dbConnection = DriverManager.getConnection(databaseConfiguration.jdbcUrl());
            }
            return dbConnection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
