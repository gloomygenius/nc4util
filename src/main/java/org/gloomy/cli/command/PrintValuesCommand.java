package org.gloomy.cli.command;

import org.gloomy.service.NetCdfService;
import org.gloomy.util.GeoUtils;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "Prints list of parameters from nc4.",
        name = "parameters",
        mixinStandardHelpOptions = true,
        version = "parameters 1.0")
public class PrintValuesCommand implements Callable<Void> {
    @Option(names = {"-s", "--source"}, description = "Full path to nc4 file")
    private String srcPath;

    @Option(names = {"-p", "--parameter"}, description = "Parameter short name (for example SWGDN)")
    private String parameter;

    @Option(names = {"-lat", "--latitude"}, description = "Latitude [-90..90]")
    private Double latitude;

    @Option(names = {"-lon", "--longitude"}, description = "Longitude in range [-180.180]")
    private Double longitude;

    @Override
    public Void call() throws Exception {
        System.out.println("Util for reading nc4 files");
        System.out.println("Developer: Vasilii Bobkov");
        System.out.println();

        NetCdfService netCdfService = new NetCdfService();
        int latIndex = GeoUtils.latIndex(latitude);
        int longIndex = GeoUtils.longIndex(longitude);
        Map<Instant, Double> instantDoubleMap = netCdfService.readData(srcPath, parameter, latIndex, longIndex);
        instantDoubleMap.forEach((time, value) -> System.out.println(time.toString() + " : " + value));
        return null;
    }
}
