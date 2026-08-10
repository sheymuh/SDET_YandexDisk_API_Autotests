package com.simbirsoft.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.List;

/**
 * ResourceGetResponse.java
 * <p>
 * DTO ответа с данными сущности ресурса Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 06.08.2026
 */
@Value
public class ResourceDataResponse {
    String path;

    String type;

    String name;

    @SerializedName("_embedded")
    Embedded embedded;

    @SerializedName("resource_id")
    String resourceId;

    @Value
    public static class Embedded {
        List<Item> items;
    }
}
