package com.simbirsoft.tests;

import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.ResourceDataResponse;
import com.simbirsoft.helpers.WaitOperationHelper;
import com.simbirsoft.helpers.FileHelper;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * BaseTest.java
 * <p>
 * Базовый тестовый класс для проверки API Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 04.08.2026
 */
public class BaseTest {
    // Список путей тестовых папок для удаления
    protected final ThreadLocal<List<String>> createdPaths = ThreadLocal.withInitial(ArrayList::new);

    @BeforeMethod
    public void setUp() {
        createdPaths.get().clear();
    }

    /**
     * Создаёт тестовую папку с уникальным названием в корне диска
     * и сохраняет её путь для дальнейшего удаления
     * @return response с данными созданного ресурса
     */
    protected ResourceDataResponse createFolder() {
        String path = "test-folder-" + UUID.randomUUID().toString().substring(0, 8);
        return createFolder(path);
    }

    /**
     * Создаёт тестовую папку и сохраняет её путь для дальнейшего удаления
     * @param path путь создаваемой папки
     * @return response с данными созданного ресурса
     */
    protected ResourceDataResponse createFolder(String path) {
        ResourceApiClient.createResource(path);
        ResourceDataResponse createdFolder = WaitOperationHelper.waitForResourceCreation(path);
        createdPaths.get().add(path);

        return createdFolder;
    }

    @AfterMethod(alwaysRun = true)
    void tearDown() {
        createdPaths.get().forEach(path -> {
            ResourceApiClient.deleteResourcePermanently(path);
            WaitOperationHelper.waitForResourceDeletion(path);
        });
        createdPaths.get().clear();
        createdPaths.remove();
    }

    @AfterSuite
    void tearDownSuite() {
        FileHelper.cleanupTestFiles();

        WaitOperationHelper.clearTrashWithRetry();
        WaitOperationHelper.waitForTrashClearing();
    }
}
