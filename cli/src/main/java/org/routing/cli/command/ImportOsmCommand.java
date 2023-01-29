package org.routing.cli.command;

import org.routing.importer.GraphImporter;
import org.routing.importer.osm.OSMQueuedImporter;
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

        GraphImporter importer = new OSMQueuedImporter();
        importer.importFromFile(Path.of(filename), null);
    }
}
