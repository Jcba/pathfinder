package org.routing.importer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            OSMImporter osmImporter = new OSMImporter();
            osmImporter.importFromFile(flevoland);
        }
    }
}