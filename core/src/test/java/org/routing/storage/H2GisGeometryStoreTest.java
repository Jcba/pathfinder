package org.routing.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.libgeo.geojson.GJAbstractGeometry;
import org.routing.libgeo.geojson.GJLineString;
import org.routing.libgeo.geojson.GJPoint;
import org.routing.libgeo.geometry.LineString;
import org.routing.libgeo.geometry.Point;
import org.routing.model.Node;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class H2GisGeometryStoreTest {

    private GeometryStore<Node> fixture;

    @BeforeEach
    void setUp() {
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(
                "jdbc:h2:~/sample.db",
                60,
                true,
                true
        );
        fixture = new H2GisGeometryStore<>(databaseConfiguration);
    }

    @Test
    void save_shouldSaveData() {
        Node node = new Node(10, new Point(0.0, 1.0));
        GJLineString GJLineString = new GJLineString(
                new LineString(List.of(new Point(1.0, 1.0), new Point(2.0, 2.0)))
        );

        fixture.save(node, GJLineString);

        GJAbstractGeometry<?> result = fixture.findById(node);

        assertThat(result).isEqualTo(GJLineString);
    }
}