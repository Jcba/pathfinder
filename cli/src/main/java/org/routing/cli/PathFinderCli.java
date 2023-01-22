package org.routing.cli;

import org.routing.cli.command.CliCommand;
import org.routing.cli.command.CliFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathFinderCli {

    private static final Logger log = LoggerFactory.getLogger(PathFinderCli.class);

    public static void main(String[] args) {
        new PathFinderCli().parse(args);
    }

    private void parse(String[] argv) {
        if (!validArguments(argv)) {
            return;
        }

        CliCommand command = CliFactory.getCommand(argv[0]);

        if (null == command) {
            log.error("Unknown command {}", argv[0]);
            return;
        }

        command.execute(argv);
    }

    private boolean validArguments(String[] argv) {
        if (argv.length < 1) {
            log.error("No command given");
            return false;
        }
        return true;
    }
}
