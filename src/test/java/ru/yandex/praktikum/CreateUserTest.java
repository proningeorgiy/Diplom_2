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

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

public class CreateUserTest {

    private static final String USEREMAIL = "testuser12345@yandex.ru";
    private static final String USERPASSWORD = "12346";
    private static final String USERNAME = "Vasja";

    String accessToken = "";

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверка кода ответа и сообщения ответа при создании пользователя")
    public void createUserTest() {

        CreateUser createUser = new CreateUser(USEREMAIL, USERPASSWORD, USERNAME);

        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken = createResponse.path("accessToken");

        //Проверка на правильный код ответа
        checkResponseCode(createResponse, SC_OK);
        //Проверка на правильный текст ответа
        checkCreateUserResponseText(createResponse);

    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Проверка кода ответа и текста ответа, при попытке создать пользователя, который уже зарегистрирован")
    public void createIdenticalUsersTest() {

        CreateUser createUser = new CreateUser(USEREMAIL, USERPASSWORD, USERNAME);

        //Создание пользователя
        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken = createResponse.path("accessToken");

        //Попытка создания уже существующего пользователя
        Response createResponseExistingUser;
        createResponseExistingUser = createUserWithResponse(createUser);

        //Проверка на правильный код ответа
        checkResponseCode(createResponseExistingUser, SC_FORBIDDEN);
        //Проверка на правильный текст ответа
        checkResponseMessageText(createResponseExistingUser);
    }

    @Step("Создание пользователя")
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

    @Step("Проверка на наличие правильного значения в ответе при создании пользователя")
    public void checkCreateUserResponseText(Response response) {
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