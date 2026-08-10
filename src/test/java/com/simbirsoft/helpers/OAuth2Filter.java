package com.simbirsoft.helpers;

import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.spi.AuthFilter;

/**
 * OAuth2Filter.java
 * <p>
 * Фильтр OAuth аутентификации
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 06.08.2026
 */
public final class OAuth2Filter implements AuthFilter {
    String accessToken;

    public OAuth2Filter(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {
        requestSpec.replaceHeader("Authorization", "Bearer " + accessToken);
        return filterContext.next(requestSpec, responseSpec);
    }
}
