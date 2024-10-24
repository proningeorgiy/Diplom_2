package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.praktikum.Order.CreateOrder;

import static io.restassured.RestAssured.given;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrderApi extends BaseApi {

    private CreateOrder createOrder;

    public void showCreateOrderRequestData() {
        System.out.println("Создание заказа");
        System.out.println("список ингридиентов " + this.createOrder.getIngredients().toString());
        System.out.println();
    }

    public void showCreateOrderResponseData(Response response) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response createOrderWithoutAuthorization() {
        showCreateOrderRequestData();

        setBaseURI();

        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .body(this.createOrder)
                .when()
                .post(ORDERS);

        return response;
    }

    public Response createOrderAuthorization(String accessToken) {
        showCreateOrderRequestData();

        setBaseURI();

        Response response;
        response = given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(this.createOrder)
                .when()
                .post(ORDERS);

        return response;
    }
}
