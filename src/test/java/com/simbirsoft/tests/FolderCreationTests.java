package com.simbirsoft.tests;

import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.ResourceDataResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * FolderCreationTests.java
 * <p>
 * Тестовый класс для проверки создания папок в API Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 07.08.2026
 */
public class FolderCreationTests extends BaseTest {

    @Test(description = "Проверка создания папки по валидному пути")
    void createFolderWithValidPath() {
        String path = "test-folder-" + UUID.randomUUID().toString().substring(0, 8);
        ResourceApiClient.createResource(path);
        createdPaths.add(path);

        String root = "disk:/";
        String directoryType = "dir";
        String responsePath = root + path;

        ResourceDataResponse getResponse = ResourceApiClient.getResource(path, 200);

        Assert.assertEquals(getResponse.getPath(), responsePath);
        Assert.assertEquals(getResponse.getType(), directoryType);
        Assert.assertEquals(getResponse.getName(), path);
    }

    @Test(description = "Проверка создания папки по пустому пути")
    void createFolderWithEmptyPath() {
        String path = "";
        int expectedCode = 400;

        ResourceApiClient.createResourceWithInvalidPath(path, expectedCode);
    }

    @Test(description = "Проверка создания папки по существующему пути")
    void createFolderWithExistingPath() {
        String path = createFolder().getPath();
        int expectedCode = 409;

        ResourceApiClient.createResourceWithInvalidPath(path, expectedCode);
    }

    @Test(description = "Проверка создания папки по слишком длинному пути (>500 символов)")
    void createFolderWithTooLongPath() {
        String path = "a".repeat(501);
        int expectedCode = 404;

        ResourceApiClient.createResourceWithInvalidPath(path, expectedCode);
    }
}
