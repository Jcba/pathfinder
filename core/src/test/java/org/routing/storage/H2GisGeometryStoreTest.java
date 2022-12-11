package org.routing.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.geometries.AbstractGeometry;
import org.routing.geometries.LineString;
import org.routing.geometries.Point;
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
        LineString lineString = new LineString(List.of(new Point(1.0, 1.0), new Point(2.0, 2.0)));

        fixture.save(node, lineString);

        AbstractGeometry<?> result = fixture.findById(node);

        assertThat(result).isEqualTo(lineString);
    }
}