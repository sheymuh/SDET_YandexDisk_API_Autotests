package com.simbirsoft.helpers;

import com.simbirsoft.dto.Item;

import java.util.List;
import java.util.Optional;

/**
 * FoldersHelper.java
 * <p>
 * Вспомогательный класс для работы с папками в API Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 10.08.2026
 */
public final class FoldersHelper {
    public static Item findResourceById(List<Item> resources, String resourceId) {
         Optional<Item> resource = resources.stream()
                 .filter(item -> item.getResourceId().equals(resourceId)).findFirst();

        return resource.orElse(null);
    }
}
