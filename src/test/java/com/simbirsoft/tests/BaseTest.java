package com.simbirsoft.tests;

import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.ResourceDataResponse;
import org.testng.annotations.AfterMethod;

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
    protected final List<String> createdPaths = new ArrayList<>();

    protected ResourceDataResponse createFolder() {
        String path = "test-folder-" + UUID.randomUUID().toString().substring(0, 8);
        ResourceApiClient.createResource(path);
        createdPaths.add(path);

        return ResourceApiClient.getResource(path, 200);
    }

    @AfterMethod
    void tearDown() {
        createdPaths.forEach(ResourceApiClient::deleteResourcePermanently);
        createdPaths.clear();

        ResourceApiClient.clearTrash();
    }
}
