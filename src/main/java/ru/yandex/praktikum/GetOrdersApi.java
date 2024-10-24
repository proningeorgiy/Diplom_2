package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static io.restassured.RestAssured.given;

@Getter
@Setter
@AllArgsConstructor
public class GetOrdersApi extends BaseApi {

    public void showGetOrdersResponseData(Response response) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response getOrdersWithoutAuthorization() {

        setBaseURI();

        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .get(ORDERS);

        return response;
    }

    public Response getOrdersAuthorization(String accessToken) {

        setBaseURI();

        Response response;
        response = given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .get(ORDERS);

        return response;
    }
}
