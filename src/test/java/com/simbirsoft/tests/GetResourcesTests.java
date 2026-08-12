package com.simbirsoft.tests;

import com.simbirsoft.api.BaseApiClient;
import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.helpers.FileHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * GetResourcesTests.java
 * <p>
 * Тестовый класс для проверки получения ресурсов в API Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 12.08.2026
 */
public class GetResourcesTests extends BaseTest {
    public static final String FILES_SCHEMA_PATH = "schemas/files-list-schema.json";

    @BeforeMethod
    void createTestData() {
        String folderPath = createFolder().getPath();

        String file1Name = "data1.txt";
        String file1Content = "spider man";
        File file1 = FileHelper.createTestFile(file1Name, file1Content);

        String filePath1 = folderPath + "/" + file1Name;
        ResourceApiClient.uploadFile(file1.getAbsolutePath(), filePath1);

        String file2Name = "data2.txt";
        String file2Content = "ninja cherePashki";
        File file2 = FileHelper.createTestFile(file2Name, file2Content);

        String filePath2 = folderPath + "/" + file2Name;
        ResourceApiClient.uploadFile(file2.getAbsolutePath(), filePath2);
    }

    @Test(description = "Проверка получения списка файлов и соответствия ответа JSON schema")
    void filesListMatchesJsonSchema() {
        given()
                .spec(BaseApiClient.SPEC)
                .get(ResourceApiClient.FILES_ENDPOINT)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(FILES_SCHEMA_PATH));
    }
}
