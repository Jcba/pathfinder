package org.routing.cli.command;

public interface CliCommand {

    /**
     * Execute the command with a set of command options
     * </p>
     * The command options will be all inputs after the command on the cli
     *
     * @param commandOptions command options
     */
    void execute(String[] commandOptions);
}
