package org.gloomy.service;

import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Сервис для работы с файлами данных в формате nc4.
 */
public class NetCdfService {
    /**
     * Метод для чтения данных из файла nc4.
     *
     * @param filePath  полный путь к файлу
     * @param parameter краткое имя параметра
     * @param latIndex  индекс широты [0..360]
     * @param longIndex индекс долготы [0..575]
     * @return справочник значений Instant (время) -> Double
     * @throws IOException если возникли проблемы с обработкой файла
     */
    public Map<Instant, Double> readData(String filePath, String parameter, int latIndex, int longIndex) throws IOException {
        try (NetcdfFile netcdfFile = NetcdfFile.open(filePath)) {
            //Ищем в файле параметр по переданному имени
            Variable variable = netcdfFile.findVariable(parameter);
            //Вытягиваем дату из файла
            String startDate = netcdfFile.findGlobalAttribute("RangeBeginningDate").getStringValue();
            //Парсим эту дату и добавляем время
            Instant startTime = LocalDate.parse(startDate).atTime(0, 30).toInstant(ZoneOffset.UTC);
            //Вытягиваем трёхмерный массив значений указанного параметра
            Array array = variable.read();
            //Получаем объект комплексного индекса (время, широта, долгота)
            Index index = array.getIndex();
            //Инициализируем справочник
            LinkedHashMap<Instant, Double> result = new LinkedHashMap<>();
            //Для каждого часа в течние дня получаем значения
            for (int timeIndex = 0; timeIndex < 24; timeIndex++) {
                Instant time = startTime.plus(timeIndex, ChronoUnit.HOURS);
                //Изменяем индекс
                Index refreshedIndex = index.set(timeIndex, latIndex, longIndex);
                //Получаем значение по индексу
                double value = array.getDouble(refreshedIndex);
                //Аккумулируем результат
                result.put(time, value);
            }
            return result;
        }
    }
}
