package org.routing.web.cli;

public class CliFactory {

    public enum Command {

        IMPORT_OSM("import-osm", new ImportOsmCommand()),
        ROUTE("route", new RouteCommand());

        private final String commandName;
        private final CliCommand command;

        Command(String name, CliCommand command) {
            this.commandName = name;
            this.command = command;
        }
    }

    public static CliCommand getCommand(String commandName) {
        for (Command value : Command.values()) {
            if(value.commandName.equalsIgnoreCase(commandName)) {
                return value.command;
            }
        }
        return null;
    }
}
