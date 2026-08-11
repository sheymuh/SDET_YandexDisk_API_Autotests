package com.simbirsoft.tests;

import com.simbirsoft.api.BaseApiClient;
import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.ResourceDataResponse;
import com.simbirsoft.helpers.AsyncOperationHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

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
        AsyncOperationHelper.waitForResourceCreation(path);
        createdPaths.get().add(path);

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

        given()
                .spec(BaseApiClient.SPEC)
                .queryParam(ResourceApiClient.PATH_PARAM, path)
                .put(ResourceApiClient.RESOURCES_ENDPOINT)
                .then()
                .statusCode(expectedCode)
                .body(ResourceApiClient.BODY_ROOT, hasKey("error"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("description"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("message"));
    }

    @Test(description = "Проверка создания папки по существующему пути")
    void createFolderWithExistingPath() {
        String path = createFolder().getPath();
        int expectedCode = 409;

        given()
                .spec(BaseApiClient.SPEC)
                .queryParam(ResourceApiClient.PATH_PARAM, path)
                .put(ResourceApiClient.RESOURCES_ENDPOINT)
                .then()
                .statusCode(expectedCode)
                .body(ResourceApiClient.BODY_ROOT, hasKey("error"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("description"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("message"));
    }

    @Test(description = "Проверка создания папки по слишком длинному пути (>500 символов)")
    void createFolderWithTooLongPath() {
        String path = "a".repeat(501);
        int expectedCode = 404;

        given()
                .spec(BaseApiClient.SPEC)
                .queryParam(ResourceApiClient.PATH_PARAM, path)
                .put(ResourceApiClient.RESOURCES_ENDPOINT)
                .then()
                .statusCode(expectedCode)
                .body(ResourceApiClient.BODY_ROOT, hasKey("error"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("description"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("message"));
    }
}
