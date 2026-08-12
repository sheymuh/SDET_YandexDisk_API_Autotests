package com.simbirsoft.tests;

import com.simbirsoft.api.BaseApiClient;
import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.helpers.FileHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

/**
 * FileTransferTests.java
 * <p>
 * Тестовый класс для проверки передачи файлов (загрузки, копирования, скачивания)
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 11.08.2026
 */
public class FileTransferTests extends BaseTest {
    @Test(description = "Проверка загрузки и затем копирования файла 2 раза в ту же папку")
    void copyFileTwiceInSameFolder() {
        String inputFolderPath = "input_data";
        String outputFolderPath = "output_data";

        createFolder(inputFolderPath);

        createFolder(outputFolderPath);

        String dataFileName = "data.txt";
        String dataFileContent = "username=SDET\npassword=secret_key";
        File dataFile = FileHelper.createTestFile(dataFileName, dataFileContent);

        String inputFilePath = inputFolderPath + "/" + dataFileName;
        ResourceApiClient.uploadFile(dataFile.getAbsolutePath(), inputFilePath);

        String outputFilePath = outputFolderPath + "/" + dataFileName;
        ResourceApiClient.copyResource(inputFilePath, outputFilePath);

        given()
                .spec(BaseApiClient.SPEC)
                .queryParam("from", inputFilePath)
                .queryParam(ResourceApiClient.PATH_PARAM, outputFilePath)
                .post(ResourceApiClient.COPY_RESOURCE_ENDPOINT)
                .then()
                .statusCode(409)
                .body(ResourceApiClient.BODY_ROOT, hasKey("error"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("description"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("message"));
    }

    @Test(description = "Проверка скачивания тестового файла")
    void downloadTextFileIsCorrect() {
        String folderPath = "sdet_data";

        createFolder(folderPath);

        String dataFileName = "data.txt";
        String dataFileContent = "username=SDET\npassword=secret_key";
        File dataFile = FileHelper.createTestFile(dataFileName, dataFileContent);

        String dataFilePath = folderPath + "/" + dataFileName;
        ResourceApiClient.uploadFile(dataFile.getAbsolutePath(), dataFilePath);

        String downloadedFileContent = ResourceApiClient.downloadFileAsString(dataFilePath);

        Assert.assertEquals(downloadedFileContent, dataFileContent,
                "Содержимое созданного файла не совпадает с содержимым скачанного файла");
    }
}
