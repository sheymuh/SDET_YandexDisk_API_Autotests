package com.simbirsoft.tests;

import com.simbirsoft.api.BaseApiClient;
import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.ResourceDataResponse;
import com.simbirsoft.helpers.AsyncOperationHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

/**
 * FolderRestoreTests.java
 * <p>
 * Тестовый класс для проверки восстановления папок из корзины в API Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 10.08.2026
 */
public class FolderRestoreTests extends BaseTest {
    /**
     * Создаёт тестовую папку и удаляет её (помещает в корзину)
     * @return список тестовых данных: путь, имя, id созданной папки и путь удалённой папки в корзине
     */
    private List<String> createAndDeleteFolder() {
        ResourceDataResponse createdFolder = createFolder();
        String createdPath = createdFolder.getPath();
        String createdName = createdFolder.getName();
        String createdResourceId = createdFolder.getResourceId();

        ResourceApiClient.deleteResource(createdPath);
        AsyncOperationHelper.waitForResourceDeletion(createdPath);
        createdPaths.get().removeLast();

        String deletedResourcePath = AsyncOperationHelper.waitForResourceInTrash(createdResourceId).getPath();

        return Arrays.asList(createdPath, createdName, createdResourceId, deletedResourcePath);
    }

    @Test(description = "Проверка восстановления папки из корзины по валидному пути")
    void restoreFolderWithValidPath() {
        List<String> testData = createAndDeleteFolder();

        String createdPath = testData.getFirst();
        String createdName = testData.get(1);
        String createdResourceId = testData.get(2);
        String deletedResourcePath = testData.getLast();

        ResourceApiClient.restoreResource(deletedResourcePath);
        ResourceDataResponse restoredResource = AsyncOperationHelper.waitForResourceRestoration(createdPath);
        createdPaths.get().add(createdPath);

        Assert.assertEquals(restoredResource.getPath(), createdPath,
                "Путь восстановленной папки не равен пути удалённой папки");
        Assert.assertEquals(restoredResource.getName(), createdName,
                "Имя восстановленной папки не равно имени удалённой папки");
        Assert.assertEquals(restoredResource.getResourceId(), createdResourceId,
                "resource_id восстановленной папки не равен resource_id удалённой папки");
    }

    @Test(description = "Проверка восстановления папки из корзины под новым именем")
    void restoreFolderWithNewName() {
        List<String> testData = createAndDeleteFolder();

        String createdResourceId = testData.get(2);
        String deletedResourcePath = testData.getLast();

        String newName = "renamed-folder-" + UUID.randomUUID().toString().substring(0, 8);
        String newPath = "disk:/" + newName;
        ResourceApiClient.restoreResourceWithNewName(deletedResourcePath, newName);
        ResourceDataResponse restoredResource = AsyncOperationHelper.waitForResourceRestoration(newPath);
        createdPaths.get().add(newPath);

        Assert.assertEquals(restoredResource.getPath(), newPath,
                "Путь восстановленной папки не содержит новое имя");
        Assert.assertEquals(restoredResource.getName(), newName,
                "Имя восстановленной папки не равно новому имени");
        Assert.assertEquals(restoredResource.getResourceId(), createdResourceId,
                "resource_id восстановленной папки не равен resource_id удалённой папки");
    }

    @Test(description = "Проверка восстановления папки по несуществующему пути")
    void restoreFolderWithNonExistingPath() {
        String nonExistingPath = "non-exist-path";
        int code = 404;

        given()
                .spec(BaseApiClient.SPEC)
                .queryParam(ResourceApiClient.PATH_PARAM, nonExistingPath)
                .put(ResourceApiClient.RESTORE_RESOURCE_ENDPOINT)
                .then()
                .statusCode(code)
                .body(ResourceApiClient.BODY_ROOT, hasKey("error"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("description"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("message"))
                .extract().response();
    }
}
