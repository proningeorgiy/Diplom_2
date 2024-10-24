package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ChangeUserDataApi extends BaseApi {

    public void showChangeUserDateRequestData(String body) {
        System.out.println("Обновленные данные пользователя");
        System.out.println("Тело запроса: " + body);
        System.out.println();
    }

    public void showChangeUserResponseData(Response response) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response changeUserDataAuthorization(String body, String accessToken) {

        showChangeUserDateRequestData(body);

        setBaseURI();

        Response response;
        response = given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(body)
                .patch(USERDATA);

        return response;
    }

    public Response changeUserDataWithoutAuthorization(String body) {

        showChangeUserDateRequestData(body);

        setBaseURI();

        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .patch(USERDATA);

        return response;
    }
}
