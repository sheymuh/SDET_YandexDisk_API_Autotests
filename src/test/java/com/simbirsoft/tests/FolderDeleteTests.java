package com.simbirsoft.tests;

import com.simbirsoft.api.BaseApiClient;
import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.Item;
import com.simbirsoft.dto.ResourceDataResponse;
import com.simbirsoft.helpers.WaitOperationHelper;
import com.simbirsoft.helpers.FoldersHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

/**
 * FolderDeleteTests.java
 * <p>
 * Тестовый класс для проверки удаления папок в API Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 07.08.2026
 */
public class FolderDeleteTests extends BaseTest {

    @Test(description = "Проверка удаления папки по валидному пути")
    void deleteFolderWithValidPath() {
        ResourceDataResponse createdFolder = createFolder();
        String createdPath = createdFolder.getPath();
        String createdName = createdFolder.getName();
        String createdResourceId = createdFolder.getResourceId();

        ResourceApiClient.deleteResource(createdPath);
        WaitOperationHelper.waitForResourceDeletion(createdPath);
        createdPaths.get().removeLast();

        Item deletedResource = WaitOperationHelper.waitForResourceInTrash(createdResourceId);

        Assert.assertNotNull(deletedResource, "В корзине не найдена удалённая папка");
        Assert.assertTrue(deletedResource.getPath().contains(createdName),
                "Путь папки в корзине не соответствует пути удалённой папки");
        Assert.assertEquals(deletedResource.getName(), createdName,
                "Имя папки в корзине не равно имени удалённой папки");
    }

    @Test(description = "Проверка удаления папки без перемещения в корзину по валидному пути")
    void deleteFolderPermanentlyWithValidPath() {
        ResourceDataResponse createdFolder = createFolder();
        String createdPath = createdFolder.getPath();
        String createdResourceId = createdFolder.getResourceId();

        ResourceApiClient.deleteResourcePermanently(createdPath);
        WaitOperationHelper.waitForResourceDeletion(createdPath);
        createdPaths.get().removeLast();

        ResourceApiClient.getResource(createdPath, 404);

        String rootFolder = "/";
        ResourceDataResponse trash = ResourceApiClient.getTrashResource(rootFolder, 200);

        List<Item> trashResources = trash.getEmbedded().getItems();

        Item deletedResource = FoldersHelper.findResourceById(trashResources, createdResourceId);

        Assert.assertNull(deletedResource, "В корзине найдена удалённая папка");
    }

    @Test(description = "Проверка удаления папки по несуществующему пути")
    void deleteFolderWithNonExistingPath() {
        String nonExistingPath = "non-exist-path";
        int code = 404;

        given()
                .spec(BaseApiClient.SPEC)
                .queryParam(ResourceApiClient.PATH_PARAM, nonExistingPath)
                .delete(ResourceApiClient.RESOURCES_ENDPOINT)
                .then()
                .statusCode(code)
                .body(ResourceApiClient.BODY_ROOT, hasKey("error"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("description"))
                .body(ResourceApiClient.BODY_ROOT, hasKey("message"));
    }
}
