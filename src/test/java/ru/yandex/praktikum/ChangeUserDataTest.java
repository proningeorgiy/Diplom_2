package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ru.yandex.praktikum.ChangeUser.ChangeUserDataResponse;
import ru.yandex.praktikum.CreateUser.CreateUser;
import ru.yandex.praktikum.LoginUser.LoginUser;

import static org.apache.http.HttpStatus.*;

public class ChangeUserDataTest {

    private static final String USEREMAIL1 = "testuser12345@yandex.ru";
    private static final String USERPASSWORD1 = "123456";
    private static final String USERNAME1 = "Vasja";

    private static final String USEREMAIL2 = "testuser1234567@yandex.ru";
    private static final String USERPASSWORD2 = "12345678";
    private static final String USERNAME2 = "VasjaPup";

    private static final String NEWUSEREMAIL = "testuser123457@yandex.ru";
    private static final String NEWUSERPASSWORD = "1234567";
    private static final String NEWUSERNAME = "VasjaP";

    String accessToken1 = "";
    String accessToken2 = "";

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Проверка кода ответа и сообщения ответа при измении данных пользователя с авторизацией")
    public void changeAutorizationUserDataTest() {

        CreateUser createUser = new CreateUser(USEREMAIL1, USERPASSWORD1, USERNAME1);
        LoginUser loginUser = new LoginUser(USEREMAIL1, USERPASSWORD1);

        //Создание пользователя
        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken1 = createResponse.path("accessToken");

        //Авторизация пользователя
        Response loginResponse;
        loginResponse = loginUserWithResponse(loginUser);

        //Изменение данных пользователя
        Response changeUserDataResponse;
        changeUserDataResponse = changeUserDataAuthorizationWithResponse();

        //Проверка на правильный код ответа
        checkResponseCode(changeUserDataResponse, SC_OK);
        //Проверка на правильный текст ответа
        checkResponseText(changeUserDataResponse);
    }

    @Test
    @DisplayName("Изменение почты пользователя с авторизацией")
    @Description("Проверка кода ответа и сообщения ответа при попытке измения почты пользователя на почту, которая уже использается")
    public void changeAutorizationUserDataExistingEmailTest() {

        CreateUser createUser1 = new CreateUser(USEREMAIL1, USERPASSWORD1, USERNAME1);
        CreateUser createUser2 = new CreateUser(USEREMAIL2, USERPASSWORD2, USERNAME2);
        LoginUser loginUser1 = new LoginUser(USEREMAIL1, USERPASSWORD1);

        //Создание первого пользователя
        Response createResponse1;
        createResponse1 = createUserWithResponse(createUser1);

        accessToken1 = createResponse1.path("accessToken");

        //Создание второго пользователя
        Response createResponse2;
        createResponse2 = createUserWithResponse(createUser2);

        accessToken2 = createResponse1.path("accessToken");

        //Авторизация пользователя
        Response loginResponse;
        loginResponse = loginUserWithResponse(loginUser1);

        //Изменение данных пользователя
        Response changeUserDataResponse;
        changeUserDataResponse = changeUserDataExistingEmailWithResponse();

        //Проверка на правильный код ответа
        checkResponseCode(changeUserDataResponse, SC_FORBIDDEN);
        //Проверка на правильный текст ответа
        checkResponseMessageExistingEmailText(changeUserDataResponse);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Проверка кода ответа и сообщения ответа при попытке измения данных пользователя без авторизации")
    public void changeWithoutAutorizationUserDataTest() throws Exception {

        CreateUser createUser = new CreateUser(USEREMAIL1, USERPASSWORD1, USERNAME1);

        //Создание пользователя
        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken1 = createResponse.path("accessToken");

        //Изменение данных пользователя
        Response changeUserDataResponse;
        changeUserDataResponse = changeUserDataWithoutAuthorizationWithResponse();

        //Проверка на правильный код ответа
        checkResponseCode(changeUserDataResponse, SC_UNAUTHORIZED);
        //Проверка на правильный текст ответа
        checkResponseMessageWithoutAutorizationText(changeUserDataResponse);
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

    @Step("Изменение данных пользователя")
    public Response changeUserDataAuthorizationWithResponse() {

        ChangeUserDataApi changeUserDataApi = new ChangeUserDataApi();
        String bodyTest = "{" + ChangeUserData.EMAIL + "\"" + NEWUSEREMAIL + "\", "
                + ChangeUserData.PASSWORD + "\"" + NEWUSERPASSWORD + "\", "
                + ChangeUserData.NAME + "\"" + NEWUSERNAME + "\"}";

        Response response;
        response = changeUserDataApi.changeUserDataAuthorization(bodyTest, accessToken1);

        changeUserDataApi.showChangeUserResponseData(response);

        return response;
    }

    @Step("Изменение данных пользователя")
    public Response changeUserDataWithoutAuthorizationWithResponse() {

        ChangeUserDataApi changeUserDataApi = new ChangeUserDataApi();
        String bodyTest = "{" + ChangeUserData.EMAIL + "\"" + NEWUSEREMAIL + "\", "
                + ChangeUserData.PASSWORD + "\"" + NEWUSERPASSWORD + "\", "
                + ChangeUserData.NAME + "\"" + NEWUSERNAME + "\"}";

        Response response;
        response = changeUserDataApi.changeUserDataWithoutAuthorization(bodyTest);

        changeUserDataApi.showChangeUserResponseData(response);

        return response;
    }

    @Step("Изменение данных пользователя")
    public Response changeUserDataExistingEmailWithResponse() {

        ChangeUserDataApi changeUserDataApi = new ChangeUserDataApi();
        String bodyTest = "{" + ChangeUserData.EMAIL + "\"" + USEREMAIL2 + "\", "
                + ChangeUserData.PASSWORD + "\"" + NEWUSERPASSWORD + "\", "
                + ChangeUserData.NAME + "\"" + NEWUSERNAME + "\"}";

        Response response;
        response = changeUserDataApi.changeUserDataAuthorization(bodyTest, accessToken1);

        changeUserDataApi.showChangeUserResponseData(response);

        return response;
    }

    @Step("Проверка на правильный код ответа")
    public void checkResponseCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkResponseText(Response response) {
        ChangeUserDataResponse changeUserDataResponse = response.body().as(ChangeUserDataResponse.class);

        Assert.assertTrue(changeUserDataResponse.isSuccess());
        Assert.assertEquals(NEWUSEREMAIL, changeUserDataResponse.getUser().getEmail());
        Assert.assertEquals(NEWUSERNAME, changeUserDataResponse.getUser().getName());
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkResponseMessageExistingEmailText(Response response) {
        Message message = response.body().as(Message.class);

        Assert.assertFalse(message.isSuccess());
        Assert.assertEquals("User with such email already exists", message.getMessage());
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkResponseMessageWithoutAutorizationText(Response response) {
        Message message = response.body().as(Message.class);

        Assert.assertFalse(message.isSuccess());
        Assert.assertEquals("You should be authorised", message.getMessage());
    }

    @After
    public void deleteUser() {
        BaseApi baseApi = new BaseApi();

        baseApi.deleteUser(accessToken1);
        baseApi.deleteUser(accessToken2);
    }
}
