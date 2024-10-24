package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ru.yandex.praktikum.CreateUser.CreateLoginUserResponse;
import ru.yandex.praktikum.CreateUser.CreateUser;
import ru.yandex.praktikum.LoginUser.LoginUser;

import static org.apache.http.HttpStatus.SC_OK;

public class LoginUserTest {

    private static final String USEREMAIL = "testuser12345@yandex.ru";
    private static final String USERPASSWORD = "12346";
    private static final String USERNAME = "Vasja";

    String accessToken = "";

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверка кода ответа и сообщения ответа при авторизации под существующим пользователем")
    public void loginUserTest() {

        CreateUser createUser = new CreateUser(USEREMAIL, USERPASSWORD, USERNAME);
        LoginUser loginUser = new LoginUser(USEREMAIL, USERPASSWORD);

        //Создание пользователя
        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken = createResponse.path("accessToken");

        //Авторизация пользователя
        Response loginResponse;
        loginResponse = loginUserWithResponse(loginUser);


        //Проверка на правильный код ответа
        checkResponseCode(loginResponse, SC_OK);
        //Проверка на правильный текст ответа
        checkLoginUserResponseText(loginResponse);
    }

    @Step("Создание пользователя")
    public Response createUserWithResponse(CreateUser createUser) {
        CreateUserApi createUserApi = new CreateUserApi(createUser);

        Response response;
        response = createUserApi.createUser();

        createUserApi.showCreateUserResponseData(response);

        return response;
    }

    @Step("Авторизация пользователя")
    public Response loginUserWithResponse(LoginUser loginUser) {
        LoginUserApi loginUserApi = new LoginUserApi(loginUser);

        Response response;
        response = loginUserApi.loginUser();

        loginUserApi.showLoginUserResponseData(response);

        return response;
    }

    @Step("Проверка на правильный код ответа")
    public void checkResponseCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Проверка на наличие правильного значения в ответе при авторизации пользователя")
    public void checkLoginUserResponseText(Response response) {
        CreateLoginUserResponse createLoginUserResponse = response.body().as(CreateLoginUserResponse.class);

        Assert.assertTrue(createLoginUserResponse.isSuccess());
        Assert.assertEquals(USEREMAIL, createLoginUserResponse.getUser().getEmail());
        Assert.assertEquals(USERNAME, createLoginUserResponse.getUser().getName());
        Assert.assertNotNull(createLoginUserResponse.getAccessToken());
        Assert.assertNotNull(createLoginUserResponse.getRefreshToken());
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkResponseMessageText(Response response) {
        Message message = response.body().as(Message.class);

        Assert.assertFalse(message.isSuccess());
        Assert.assertEquals("User already exists", message.getMessage());
    }

    @After
    public void deleteUser() {
        BaseApi baseApi = new BaseApi();

        baseApi.deleteUser(accessToken);
    }
}