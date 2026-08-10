package com.simbirsoft.dto;

import lombok.Value;

/**
 * ResourceActionResponse.java
 * <p>
 * DTO ответа на запрос действия с ресурсом (создание, удаление, перемещение, публикация)
 * для сущности ресурса Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 06.08.2026
 */
@Value
public class ResourceActionResponse {
    String method;

    String href;

    Boolean templated;
}
