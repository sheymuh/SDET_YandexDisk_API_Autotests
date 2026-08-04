package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

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
    protected static final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(ParameterProvider.get("base.url"))
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
}
