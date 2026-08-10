package com.simbirsoft.dto;

import lombok.Value;

/**
 * ErrorResponse.java
 * <p>
 * DTO ответа с ошибкой для Wordpress
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 06.08.2026
 */
@Value
public class ErrorResponse {
    String error;

    String description;

    String message;
}
