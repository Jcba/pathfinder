package org.routing.cli.command;

import org.routing.importer.GraphImporter;
import org.routing.importer.osm.OSMImporter;
import org.routing.model.Edge;
import org.routing.model.MemoryGraph;
import org.routing.storage.DatabaseConfiguration;
import org.routing.storage.GeometryStore;
import org.routing.storage.H2GisGeometryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;

public class ImportOsmCommand implements CliCommand {

    private static final Logger log = LoggerFactory.getLogger(ImportOsmCommand.class);

    @Override
    public void execute(String[] commandOptions) {
        log.info("Starting OSM import with command options {}", Arrays.stream(commandOptions).toList());

        String filename = commandOptions[1];

        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(
                "jdbc:h2:~/edges.db",
                60,
                true,
                true
        );

        GeometryStore<Edge> edgeGeometryStore = new H2GisGeometryStore<>(databaseConfiguration);

        GraphImporter importer = new OSMImporter(edgeGeometryStore);
        importer.importFromFile(Path.of(filename), new MemoryGraph());
    }
}
