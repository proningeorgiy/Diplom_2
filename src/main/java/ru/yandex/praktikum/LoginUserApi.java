package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.praktikum.LoginUser.LoginUser;

import static io.restassured.RestAssured.given;

@Getter
@Setter
@AllArgsConstructor
public class LoginUserApi extends BaseApi {

    private LoginUser loginUser;

    public void showLoginUserRequestData() {
        System.out.println("Авторизация пользователя");
        System.out.println("Email: " + this.loginUser.getEmail());
        System.out.println("Пароль: " + this.loginUser.getPassword());
        System.out.println();
    }

    public void showLoginUserResponseData(Response response) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response loginUser() {
        showLoginUserRequestData();

        setBaseURI();

        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .body(this.loginUser)
                .when()
                .post(LOGINUSER);

        return response;
    }
}
