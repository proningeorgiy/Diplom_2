package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class BaseApi {

    final String CREATEUSER = "/api/auth/register";
    final String LOGINUSER = "/api/auth/login";
    final String USERDATA = "/api/auth/user";
    final String ORDERS = "api/orders";

    public void setBaseURI() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    public void deleteUser(String accessToken) {
        if (!accessToken.isEmpty())
            given()
                    .header("Authorization", accessToken)
                    .contentType(ContentType.JSON)
                    .delete(USERDATA);
    }
}