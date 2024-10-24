package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.CreateUser.CreateUser;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;

@RunWith(Parameterized.class)
public class CreateUserParamTest {

    private String email;
    private String password;
    private String name;

    public CreateUserParamTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] requisites() {
        return new Object[][]{
                {"testuser12345@yandex.ru", "12346", ""},
                {"testuser12345@yandex.ru", "", "Vasja"},
                {"", "12346", "Vasja"},
        };
    }

    @Test
    @DisplayName("Попытка создания пользователя с неполными исходными данными")
    @Description("Проверка кода ответа и сообщения ответа при попытке создания пользователя с неполными исходными данными")
    public void createWrongUserTest() {

        CreateUser createUser = new CreateUser(email, password, name);

        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        //Проверка на правильный код ответа
        checkResponseCode(createResponse, SC_FORBIDDEN);
        //Проверка на правильный текст ответа
        checkResponseMessageText(createResponse);

    }

    @Step("Попытка создания пользователя")
    public Response createUserWithResponse(CreateUser createUser) {
        CreateUserApi createUserApi = new CreateUserApi(createUser);

        Response response;
        response = createUserApi.createUser();

        createUserApi.showCreateUserResponseData(response);

        return response;
    }

    @Step("Проверка на правильный код ответа")
    public void checkResponseCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkResponseMessageText(Response response) {
        Message message = response.body().as(Message.class);

        Assert.assertFalse(message.isSuccess());
        Assert.assertEquals("Email, password and name are required fields", message.getMessage());
    }

}
