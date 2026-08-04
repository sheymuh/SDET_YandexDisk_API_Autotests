package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

/**
 * AuthTests.java
 * <p>
 * Тестовый класс для проверки авторизации в API Яндекс.Диска
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 04.08.2026
 */
public class AuthTests extends BaseTest {
    private final String DISK_PATH = "v1/disk/";
    private final String ROOT = "$";

    @Test(description = "Проверка авторизации с валидным OAuth токеном")
    void authWithValidToken() {
        String login = "andreyblack45";
        String displayName = "Шеймухов Андрей";

        given()
                .spec(spec)
                .auth().oauth2(ParameterProvider.get("auth.token"))
                .get(DISK_PATH)
                .then()
                .statusCode(200)
                .body(ROOT, hasKey("user"))
                .body("user.login", equalTo(login))
                .body("user.display_name", equalTo(displayName));
    }

    @Test(description = "Проверка авторизации без токена")
    void authWithoutToken() {
        given()
                .spec(spec)
                .get(DISK_PATH)
                .then()
                .statusCode(401)
                .body(ROOT, hasKey("error"))
                .body(ROOT, hasKey("description"))
                .body(ROOT, hasKey("message"));
    }
}
