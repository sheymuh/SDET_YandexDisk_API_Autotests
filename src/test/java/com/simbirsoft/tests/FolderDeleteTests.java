package com.simbirsoft.tests;

import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.Item;
import com.simbirsoft.dto.ResourceDataResponse;
import com.simbirsoft.helpers.FoldersHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

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
        createdPaths.removeLast();

        ResourceApiClient.getResource(createdPath, 404);

        String rootFolder = "/";
        ResourceDataResponse trash = ResourceApiClient.getTrashResource(rootFolder, 200);

        List<Item> trashResources = trash.getEmbedded().getItems();

        Item deletedResource = FoldersHelper.findResourceById(trashResources, createdResourceId);

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
        createdPaths.removeLast();

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

        ResourceApiClient.deleteResourceWithInvalidPath(nonExistingPath, code);
    }
}
