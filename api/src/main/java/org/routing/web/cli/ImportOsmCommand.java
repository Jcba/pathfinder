package org.routing.web.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ImportOsmCommand implements CliCommand {

    private static final Logger log = LoggerFactory.getLogger(ImportOsmCommand.class);

    @Override
    public void execute(String[] commandOptions) {
        log.info("Starting OSM import with command options {}", Arrays.stream(commandOptions).toList());
    }
}
