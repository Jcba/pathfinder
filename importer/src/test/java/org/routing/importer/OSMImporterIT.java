package org.routing.importer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.libgeo.geojson.Point;
import org.routing.importer.osm.OSMImporter;
import org.routing.model.Edge;
import org.routing.model.Graph;
import org.routing.model.MemoryGraph;
import org.routing.storage.DatabaseConfiguration;
import org.routing.storage.GeometryKeyReference;
import org.routing.storage.H2GisGeometryStore;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class OSMImporterIT {

    @BeforeEach
    void setUp() {
    }

    @Test
    void readFlevoland() {
        Graph resultGraph = new MemoryGraph();
        Point pointInFlevoland = new Point(52.347793, 5.267263);
        String flevolandOSMFileName = "flevoland-latest.osm.pbf";

        Path flevoland = getPathFromResource(flevolandOSMFileName);

        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(
                "jdbc:h2:mem:test",
                60,
                true,
                true
        );
        H2GisGeometryStore<Edge> edgeGeometryStore = new H2GisGeometryStore<>(databaseConfiguration);
        OSMImporter osmImporter = new OSMImporter(edgeGeometryStore);

        // do the import
        osmImporter.importFromFile(flevoland, resultGraph);

        // check result
        GeometryKeyReference closestEdge = edgeGeometryStore.findClosest(pointInFlevoland);

        assertThat(resultGraph).isNotNull()
                .extracting(g -> g.findEdge(closestEdge))
                .isNotNull();

    }

    private Path getPathFromResource(String resourceName) {
        URL resource = getClass().getClassLoader().getResource(resourceName);
        if (null == resource) {
            throw new IllegalStateException("could not load resource");
        }

        try {
            return Path.of(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("could not load resource", e);
        }
    }
}