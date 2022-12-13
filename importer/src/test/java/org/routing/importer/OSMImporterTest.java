package org.routing.importer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.routing.model.Edge;
import org.routing.model.MemoryGraph;
import org.routing.storage.DatabaseConfiguration;
import org.routing.storage.GeometryStore;
import org.routing.storage.H2GisGeometryStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

class OSMImporterTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void readFlevoland() throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource("flevoland-latest.osm.pbf");
        if (null != resource) {
            Path flevoland = Path.of(resource.toURI());
            DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(
                    "jdbc:h2:~/test",
                    60,
                    true,
                    true
            );
            GeometryStore<Edge> edgeGeometryStore = new H2GisGeometryStore<>(databaseConfiguration);
            OSMImporter osmImporter = new OSMImporter(edgeGeometryStore);
            osmImporter.importFromFile(flevoland, new MemoryGraph());
        }
    }
}