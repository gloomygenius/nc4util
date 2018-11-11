package org.gloomy.cli;

import org.gloomy.cli.command.PrintValuesCommand;
import picocli.CommandLine;

public class Application {
    public static void main(String[] args) {
        CommandLine.call(new PrintValuesCommand(), args);
    }
}