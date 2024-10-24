package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.praktikum.CreateUser.CreateUser;

import static io.restassured.RestAssured.given;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserApi extends BaseApi {

    private CreateUser createUser;

    public void showCreateUserRequestData() {
        System.out.println("Создание пользователя");
        System.out.println("Имя: " + this.createUser.getName());
        System.out.println("Пароль: " + this.createUser.getPassword());
        System.out.println("Email: " + this.createUser.getEmail());
        System.out.println();
    }

    public void showCreateUserResponseData(Response response) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response createUser() {
        showCreateUserRequestData();

        setBaseURI();

        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .body(this.createUser)
                .when()
                .post(CREATEUSER);

        return response;
    }
}
