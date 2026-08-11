package com.simbirsoft.api;

import com.google.gson.Gson;
import com.simbirsoft.dto.ResourceActionResponse;
import com.simbirsoft.dto.ResourceDataResponse;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * CategoryApiClient.java
 * <p>
 * Класс для работы с API методами ресурсов Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 06.08.2026
 */
public class ResourceApiClient extends BaseApiClient {
    public static final String RESOURCES_ENDPOINT = "resources/";
    public static final String RESOURCES_TRASH_ENDPOINT = "trash/resources/";
    public static final String RESTORE_RESOURCE_ENDPOINT = RESOURCES_TRASH_ENDPOINT + "restore";
    public static final String BODY_ROOT = "$";
    public static final String PATH_PARAM = "path";

    private static Gson gson = new Gson();

    public static int getResourceStatusCode(String path) {
        return given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .get(RESOURCES_ENDPOINT)
                .getStatusCode();
    }

    public static ResourceDataResponse getResource(String path, int code) {
        Response response = given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .get(RESOURCES_ENDPOINT)
                .then()
                .statusCode(code)
                .extract().response();

        return gson.fromJson(response.getBody().asString(), ResourceDataResponse.class);
    }

    public static ResourceDataResponse getTrashResource(String path, int code) {
        Response response = given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .get(RESOURCES_TRASH_ENDPOINT)
                .then()
                .statusCode(code)
                .extract().response();

        return gson.fromJson(response.getBody().asString(), ResourceDataResponse.class);
    }

    public static ResourceActionResponse createResource(String path) {
        Response response = given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .put(RESOURCES_ENDPOINT)
                .then()
                .statusCode(201)
                .body(BODY_ROOT, hasKey("method"))
                .body(BODY_ROOT, hasKey("href"))
                .body(BODY_ROOT, hasKey("templated"))
                .extract().response();

        return gson.fromJson(response.getBody().asString(), ResourceActionResponse.class);
    }

    public static void deleteResource(String path) {
        given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .delete(RESOURCES_ENDPOINT)
                .then()
                .statusCode(204);
    }

    public static void deleteResourcePermanently(String path) {
        given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .queryParam("permanently", true)
                .delete(RESOURCES_ENDPOINT)
                .then()
                .statusCode(204);
    }

    public static void clearTrash() {
        given()
                .spec(SPEC)
                .delete(RESOURCES_TRASH_ENDPOINT)
                .then()
                .statusCode(anyOf(is(204), is(202)));
    }

    public static ResourceActionResponse restoreResource(String path) {
        Response response = given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .put(RESTORE_RESOURCE_ENDPOINT)
                .then()
                .statusCode(201)
                .body(BODY_ROOT, hasKey("method"))
                .body(BODY_ROOT, hasKey("href"))
                .body(BODY_ROOT, hasKey("templated"))
                .extract().response();

        return gson.fromJson(response.getBody().asString(), ResourceActionResponse.class);
    }

    public static ResourceActionResponse restoreResourceWithNewName(String path, String name) {
        Response response = given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .queryParam("name", name)
                .put(RESTORE_RESOURCE_ENDPOINT)
                .then()
                .statusCode(201)
                .body(BODY_ROOT, hasKey("method"))
                .body(BODY_ROOT, hasKey("href"))
                .body(BODY_ROOT, hasKey("templated"))
                .extract().response();

        return gson.fromJson(response.getBody().asString(), ResourceActionResponse.class);
    }
}
