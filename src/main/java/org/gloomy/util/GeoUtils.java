package org.gloomy.util;

public class GeoUtils {
    /**
     * Метод для перевода широты в целочисленный индекс [-90.0..90.0] -> [0..360]
     */
    public static int latIndex(double latitude) {
        double doubleIndex = (latitude + 90) * 2;
        return (int) Math.round(doubleIndex);
    }

    /**
     * Метод для перевода широты в целочисленный индекс [-180.0..180.0] -> [0..575]
     */
    public static int longIndex(double longitude) {

        double doubleIndex = (longitude + 180) / 0.625;
        return (int) Math.round(doubleIndex);
    }

    public static double transformIndexToLat(int index) {
        return (index / 2.0 - 90);
    }

    public static double transformIndexToLon(int index) {
        return index * 0.625 - 180;
    }
}
