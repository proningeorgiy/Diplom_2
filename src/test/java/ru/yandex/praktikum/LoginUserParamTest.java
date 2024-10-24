package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.CreateUser.CreateUser;
import ru.yandex.praktikum.LoginUser.LoginUser;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RunWith(Parameterized.class)
public class LoginUserParamTest {

    private static final String USEREMAIL = "testuser12345@yandex.ru";
    private static final String USERPASSWORD = "12346";
    private static final String USERNAME = "Vasja";

    String accessToken = "";


    private String email;
    private String password;

    public LoginUserParamTest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] requisites() {
        return new Object[][]{
                {"testuser12345@yandex.ru", "123467"},
                {"testuser123451@yandex.ru", "13246"},
        };
    }

    @Test
    @DisplayName("Попытка авторизации пользователя с неверным логином и паролем")
    @Description("Проверка кода ответа и сообщения ответа при попытке авторизации пользователя с неверным логином и паролем")
    public void loginWrongUserTest() {

        CreateUser createUser = new CreateUser(USEREMAIL, USERPASSWORD, USERNAME);
        LoginUser loginUser = new LoginUser(email, password);

        //Создание пользователя
        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken = createResponse.path("accessToken");

        //Авторизация пользователя
        Response loginResponse;
        loginResponse = loginUserWithResponse(loginUser);

        //Проверка на правильный код ответа
        checkResponseCode(loginResponse, SC_UNAUTHORIZED);
        //Проверка на правильный текст ответа
        checkResponseMessageText(loginResponse);
    }

    @Step("Попытка создания пользователя")
    public Response createUserWithResponse(CreateUser createUser) {
        CreateUserApi createUserApi = new CreateUserApi(createUser);

        Response response;
        response = createUserApi.createUser();

        createUserApi.showCreateUserResponseData(response);

        return response;
    }

    @Step("Попытка авторизации пользователя")
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

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkResponseMessageText(Response response) {
        Message message = response.body().as(Message.class);

        Assert.assertFalse(message.isSuccess());
        Assert.assertEquals("email or password are incorrect", message.getMessage());
    }

    @After
    public void deleteUser() {
        BaseApi baseApi = new BaseApi();

        baseApi.deleteUser(accessToken);
    }
}
