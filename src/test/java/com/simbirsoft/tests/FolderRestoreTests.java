package com.simbirsoft.tests;

import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.Item;
import com.simbirsoft.dto.ResourceDataResponse;
import com.simbirsoft.helpers.FoldersHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    private List<String> createAndDeleteFolder() {
        ResourceDataResponse createdFolder = createFolder();
        String createdPath = createdFolder.getPath();
        String createdName = createdFolder.getName();
        String createdResourceId = createdFolder.getResourceId();

        ResourceApiClient.deleteResource(createdPath);
        createdPaths.removeLast();

        String rootFolder = "/";
        ResourceDataResponse trash = ResourceApiClient.getTrashResource(rootFolder, 200);

        List<Item> trashResources = trash.getEmbedded().getItems();

        String deletedResourcePath = FoldersHelper.findResourceById(trashResources, createdResourceId).getPath();

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
        createdPaths.add(createdPath);

        ResourceDataResponse restoredResource = ResourceApiClient.getResource(createdPath, 200);

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
        createdPaths.add(newPath);

        ResourceDataResponse restoredResource = ResourceApiClient.getResource(newName, 200);

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

        ResourceApiClient.restoreResourceWithInvalidPath(nonExistingPath, code);
    }
}
