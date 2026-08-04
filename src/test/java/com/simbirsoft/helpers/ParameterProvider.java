package com.simbirsoft.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * ParameterProvider.java
 * <p>
 * Вспомогательный класс для работы с параметрами из файла config.properties
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 04.08.2026
 */
public class ParameterProvider {
    private static final String PARAMETERS_PATH = "configurations/config.properties";
    private static final ParameterProvider INSTANCE = new ParameterProvider();
    private final HashMap<String, String> parameters;

    private ParameterProvider() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PARAMETERS_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Файл " + PARAMETERS_PATH + " не найден");
            }

            Properties prop = new Properties();
            prop.load(inputStream);
            parameters = new HashMap<>();
            prop.stringPropertyNames()
                    .forEach(key -> parameters.put(key, prop.getProperty(key)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return INSTANCE.parameters.get(key);
    }
}
