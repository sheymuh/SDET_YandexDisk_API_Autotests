package com.simbirsoft.api;

import com.simbirsoft.helpers.OAuth2Filter;
import com.simbirsoft.helpers.ParameterProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * BaseApiClient.java
 * <p>
 * Базовый класс для работы с API методами Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 06.08.2026
 */
public class BaseApiClient {
    private static OAuth2Filter setOAuthFilter() {
        return new OAuth2Filter(ParameterProvider.get("auth.token"));
    }

    public static final RequestSpecification SPEC = new RequestSpecBuilder()
            .setBaseUri(ParameterProvider.get("base.url"))
            .addFilter(setOAuthFilter())
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
}
