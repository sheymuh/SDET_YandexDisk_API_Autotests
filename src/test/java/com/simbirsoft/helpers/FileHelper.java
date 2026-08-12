package com.simbirsoft.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * FileHelper.java
 * <p>
 * Вспомогательный класс для работы с файлами
 */
public final class FileHelper {
    private static final String TEST_FILES_RELATIVE_PATH = "src/test/resources/test-files/";

    public static File createTestFile(String fileName, String content) {
        try {
            Path dir = Path.of(TEST_FILES_RELATIVE_PATH);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            File file = new File(TEST_FILES_RELATIVE_PATH + fileName);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            }

            return file;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать тестовый файл: " + fileName + "./n" + e.getMessage(), e);
        }
    }

    public static void cleanupTestFiles() {
        Path dir = Path.of(TEST_FILES_RELATIVE_PATH);

        if (Files.exists(dir)) {
            try (Stream<Path> walk = Files.walk(dir)) {
                walk.filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("Не удалось удалить файл: " + path);
                            }
                        });
            } catch (IOException e) {
                System.err.println("Не удалось очистить тестовые файлы");
            }
        }
    }
}
