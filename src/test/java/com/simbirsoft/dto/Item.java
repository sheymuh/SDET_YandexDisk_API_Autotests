package com.simbirsoft.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

/**
 * Item.java
 * <p>
 * DTO ответа с данными ресурса в папке Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 06.08.2026
 */
@Value
public class Item {
    String path;

    String type;

    String name;

    @SerializedName("resource_id")
    String resourceId;
}
