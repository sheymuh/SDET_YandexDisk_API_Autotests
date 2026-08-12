package com.simbirsoft.helpers;

import com.simbirsoft.api.BaseApiClient;
import com.simbirsoft.api.ResourceApiClient;
import com.simbirsoft.dto.Item;
import com.simbirsoft.dto.ResourceDataResponse;
import org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

/**
 * AsyncOperationHelper.java
 * <p>
 * Вспомогательный класс для работы с асинхронными операциями Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 10.08.2026
 */
public final class WaitOperationHelper {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    static {
        Awaitility.setDefaultTimeout(DEFAULT_TIMEOUT);
        Awaitility.setDefaultPollInterval(fibonacci(MILLISECONDS));
    }

    public static ResourceDataResponse waitForResourceCreation(String path) {
        return await()
                .ignoreExceptions()
                .until(
                        () -> ResourceApiClient.getResource(path, 200),
                        Objects::nonNull
                );
    }

    public static boolean waitForResourceDeletion(String path) {
        return await()
                .until(
                        () -> ResourceApiClient.getResourceStatusCode(path) == 404,
                        status -> status
                );
    }

    public static Item waitForResourceInTrash(String resourceId) {
        return await()
                .ignoreExceptions()
                .pollDelay(Duration.ofSeconds(1))
                .until(
                        () -> {
                            ResourceDataResponse trash = ResourceApiClient.getTrashResource("/", 200);

                            List<Item> resources = trash.getEmbedded().getItems();
                            return FoldersHelper.findResourceById(resources, resourceId);
                        },
                        Objects::nonNull
                );
    }

    public static ResourceDataResponse waitForResourceRestoration(String path) {
        return await()
                .ignoreExceptions()
                .until(
                        () -> ResourceApiClient.getResource(path, 200),
                        Objects::nonNull
                );
    }

    public static boolean waitForTrashClearing() {
        return await()
                .until(
                        () -> {
                                try {
                                ResourceDataResponse trash = ResourceApiClient.getTrashResource("/", 200);
                                List<Item> resources = trash.getEmbedded().getItems();

                                return resources.isEmpty();
                            } catch (Exception e) {
                                return false;
                            }
                        },
                        status -> status
                );
    }

    public static void clearTrashWithRetry() {
        await()
                .atMost(Duration.ofSeconds(60))
                .pollInterval(Duration.ofSeconds(5))
                .ignoreExceptions()
                .until(() -> {
                    int status = given()
                            .spec(BaseApiClient.SPEC)
                            .delete(ResourceApiClient.RESOURCES_TRASH_ENDPOINT)
                            .getStatusCode();
                    return status == 204 || status == 202;
                });
    }
}
