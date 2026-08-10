package com.simbirsoft.tests;

import com.simbirsoft.api.BaseApiClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * AuthTests.java
 * <p>
 * Тестовый класс для проверки авторизации в API Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 04.08.2026
 */
public class AuthTests {
    @Test(description = "Проверка авторизации с валидным OAuth токеном")
    void authWithValidToken() {
        String login = "andreyblack45";
        String displayName = "Шеймухов Андрей";

        Response response = given()
                .spec(BaseApiClient.SPEC)
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        String userField = response.jsonPath().getString("user");
        String loginField = response.jsonPath().getString("user.login");
        String displayNameField = response.jsonPath().getString("user.display_name");

        Assert.assertNotNull(userField, "Тело ответа не содержит поля user");
        Assert.assertEquals(loginField, login,
                "Поле user.login не соответствует данным пользователя");
        Assert.assertEquals(displayNameField, displayName,
                "Поле user.display_name не соответствует данным пользователя");
    }

    @Test(description = "Проверка авторизации без токена")
    void authWithoutToken() {
        Response response = given()
                .spec(BaseApiClient.SPEC)
                .auth().none()
                .get()
                .then()
                .statusCode(401)
                .extract().response();

        String errorField = response.jsonPath().getString("error");
        String descriptionField = response.jsonPath().getString("description");
        String messageField = response.jsonPath().getString("message");

        Assert.assertNotNull(errorField, "Тело ответа не содержит поля error");
        Assert.assertNotNull(descriptionField, "Тело ответа не содержит поля description");
        Assert.assertNotNull(messageField, "Тело ответа не содержит поля message");
    }
}
