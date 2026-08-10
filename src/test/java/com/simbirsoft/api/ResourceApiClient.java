package com.simbirsoft.api;

import com.google.gson.Gson;
import com.simbirsoft.dto.ErrorResponse;
import com.simbirsoft.dto.ResourceActionResponse;
import com.simbirsoft.dto.ResourceDataResponse;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

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
    private static final String RESOURCES_ENDPOINT = "resources/";
    private static final String RESOURCES_TRASH_ENDPOINT = "trash/resources/";
    private static final String RESTORE_RESOURCE_ENDPOINT = RESOURCES_TRASH_ENDPOINT + "restore";
    private static final String BODY_ROOT = "$";
    private static final String PATH_PARAM = "path";

    private static Gson gson = new Gson();

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

    public static ErrorResponse createResourceWithInvalidPath(String path, int code) {
        Response response = given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .put(RESOURCES_ENDPOINT)
                .then()
                .statusCode(code)
                .body(BODY_ROOT, hasKey("error"))
                .body(BODY_ROOT, hasKey("description"))
                .body(BODY_ROOT, hasKey("message"))
                .extract().response();

        return gson.fromJson(response.getBody().asString(), ErrorResponse.class);
    }

    public static void deleteResource(String path) {
        given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .delete(RESOURCES_ENDPOINT)
                .then()
                .statusCode(204);
    }

    public static ErrorResponse deleteResourceWithInvalidPath(String path, int code) {
        Response response = given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .delete(RESOURCES_ENDPOINT)
                .then()
                .statusCode(code)
                .body(BODY_ROOT, hasKey("error"))
                .body(BODY_ROOT, hasKey("description"))
                .body(BODY_ROOT, hasKey("message"))
                .extract().response();

        return gson.fromJson(response.getBody().asString(), ErrorResponse.class);
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
                .queryParam("force_async", false)
                .delete(RESOURCES_TRASH_ENDPOINT)
                .then()
                .statusCode(204);
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

    public static ErrorResponse restoreResourceWithInvalidPath(String path, int code) {
        Response response = given()
                .spec(SPEC)
                .queryParam(PATH_PARAM, path)
                .put(RESTORE_RESOURCE_ENDPOINT)
                .then()
                .statusCode(code)
                .body(BODY_ROOT, hasKey("error"))
                .body(BODY_ROOT, hasKey("description"))
                .body(BODY_ROOT, hasKey("message"))
                .extract().response();

        return gson.fromJson(response.getBody().asString(), ErrorResponse.class);
    }
}
