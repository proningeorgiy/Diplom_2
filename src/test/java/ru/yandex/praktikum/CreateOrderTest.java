package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ru.yandex.praktikum.CreateUser.CreateUser;
import ru.yandex.praktikum.LoginUser.LoginUser;
import ru.yandex.praktikum.Order.CreateOrder;
import ru.yandex.praktikum.Order.CreateOrderAuthorizationResponse;
import ru.yandex.praktikum.Order.CreateOrderWithoutAuthorizationResponse;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {

    private static final String USEREMAIL = "testuser12345@yandex.ru";
    private static final String USERPASSWORD = "12346";
    private static final String USERNAME = "Vasja";

    String accessToken = "";

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизованным пользователем")
    @Description("Проверка кода ответа и сообщения ответа при создании заказа с ингредиентами авторизованным пользователем")
    public void createOrderUserAuthorizationTest() {

        CreateUser createUser = new CreateUser(USEREMAIL, USERPASSWORD, USERNAME);
        LoginUser loginUser = new LoginUser(USEREMAIL, USERPASSWORD);

        CreateOrder createOrder = new CreateOrder(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72"));

        //Создание пользователя
        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken = createResponse.path("accessToken");

        //Авторизация пользователя
        Response loginResponse;
        loginResponse = loginUserWithResponse(loginUser);

        //Создание заказа
        Response createOrderResponse;
        createOrderResponse = createOrderAuthorizationWithResponse(createOrder);

        //Проверка на правильный код ответа
        checkResponseCode(createOrderResponse, SC_OK);
        //Проверка на правильный текст ответа
        checkCreateOrderAuthorizationResponseText(createOrderResponse);

    }

    @Test
    @DisplayName("Создание заказа с ингредиентами неавторизованным пользователем")
    @Description("Проверка кода ответа и сообщения ответа при создания заказа с ингредиентами неавторизованным пользователем")
    public void createOrderUserWithoutAuthorizationTest() {

        CreateOrder createOrder = new CreateOrder(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72"));

        //Создание заказа
        Response createOrderResponse;
        createOrderResponse = createOrderWithoutAuthorizationWithResponse(createOrder);

        //Проверка на правильный код ответа
        checkResponseCode(createOrderResponse, SC_OK);
        //Проверка на правильный текст ответа
        checkCreateOrderWithoutAuthorizationResponseText(createOrderResponse);

    }

    @Test
    @DisplayName("Попытка создания заказа без ингредиентов")
    @Description("Проверка кода ответа и сообщения ответа при попытке создания заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {

        CreateUser createUser = new CreateUser(USEREMAIL, USERPASSWORD, USERNAME);
        LoginUser loginUser = new LoginUser(USEREMAIL, USERPASSWORD);

        CreateOrder createOrder = new CreateOrder(List.of());

        //Создание пользователя
        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken = createResponse.path("accessToken");

        //Авторизация пользователя
        Response loginResponse;
        loginResponse = loginUserWithResponse(loginUser);

        //Создание заказа
        Response createOrderResponse;
        createOrderResponse = createOrderAuthorizationWithResponse(createOrder);

        //Проверка на правильный код ответа
        checkResponseCode(createOrderResponse, SC_BAD_REQUEST);
        //Проверка на правильный текст ответа
        checkCreateOrderWithoutIngredientsResponseText(createOrderResponse);
    }

    @Test
    @DisplayName("Попытка создания заказа с неверным ингредиентом")
    @Description("Проверка кода ответа и сообщения ответа при попытке создания заказа с неверным ингредиентом")
    public void createOrderWrongIngredientTest() {

        CreateUser createUser = new CreateUser(USEREMAIL, USERPASSWORD, USERNAME);
        LoginUser loginUser = new LoginUser(USEREMAIL, USERPASSWORD);

        CreateOrder createOrder = new CreateOrder(Arrays.asList("61c0c5a71d1f82001bdaaa6", "61c0c5a71d1f82001bdaaa72"));

        //Создание пользователя
        Response createResponse;
        createResponse = createUserWithResponse(createUser);

        accessToken = createResponse.path("accessToken");

        //Авторизация пользователя
        Response loginResponse;
        loginResponse = loginUserWithResponse(loginUser);

        //Создание заказа
        Response createOrderResponse;
        createOrderResponse = createOrderAuthorizationWithResponse(createOrder);

        //Проверка на правильный код ответа
        checkResponseCode(createOrderResponse, SC_INTERNAL_SERVER_ERROR);
        //Проверка на правильный текст ответа
        checkCreateOrderWrongIngredientResponseText(createOrderResponse);

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

    @Step("Создание заказа")
    public Response createOrderWithoutAuthorizationWithResponse(CreateOrder createOrder) {
        CreateOrderApi createOrderApi = new CreateOrderApi(createOrder);

        Response response;
        response = createOrderApi.createOrderWithoutAuthorization();

        createOrderApi.showCreateOrderResponseData(response);

        return response;
    }

    @Step("Создание заказа")
    public Response createOrderAuthorizationWithResponse(CreateOrder createOrder) {
        CreateOrderApi createOrderApi = new CreateOrderApi(createOrder);

        Response response;
        response = createOrderApi.createOrderAuthorization(accessToken);

        createOrderApi.showCreateOrderResponseData(response);

        return response;
    }

    @Step("Проверка на правильный код ответа")
    public void checkResponseCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkCreateOrderAuthorizationResponseText(Response response) {
        CreateOrderAuthorizationResponse createOrderAuthorizationResponse = response.body().as(CreateOrderAuthorizationResponse.class);

        Assert.assertTrue(createOrderAuthorizationResponse.isSuccess());
        Assert.assertNotNull(createOrderAuthorizationResponse.getName());
        Assert.assertTrue(createOrderAuthorizationResponse.getOrder().getIngredients().size() > 0);
        Assert.assertNotNull(createOrderAuthorizationResponse.getOrder().get_id());
        Assert.assertEquals(USERNAME, createOrderAuthorizationResponse.getOrder().getOwner().getName());
        Assert.assertEquals(USEREMAIL, createOrderAuthorizationResponse.getOrder().getOwner().getEmail());
        Assert.assertNotNull(createOrderAuthorizationResponse.getOrder().getOwner().getCreatedAt());
        Assert.assertNotNull(createOrderAuthorizationResponse.getOrder().getOwner().getUpdatedAt());
        Assert.assertEquals("done", createOrderAuthorizationResponse.getOrder().getStatus());
        Assert.assertNotNull(createOrderAuthorizationResponse.getOrder().getName());
        Assert.assertNotNull(createOrderAuthorizationResponse.getOrder().getCreatedAt());
        Assert.assertNotNull(createOrderAuthorizationResponse.getOrder().getUpdatedAt());
        Assert.assertNotNull(createOrderAuthorizationResponse.getOrder().getNumber());
        Assert.assertNotNull(createOrderAuthorizationResponse.getOrder().getPrice());
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkCreateOrderWithoutAuthorizationResponseText(Response response) {
        CreateOrderWithoutAuthorizationResponse createOrderWithoutAuthorizationResponse = response.body().as(CreateOrderWithoutAuthorizationResponse.class);

        Assert.assertNotNull(createOrderWithoutAuthorizationResponse.getName());
        Assert.assertNotNull(createOrderWithoutAuthorizationResponse.getOrder().getNumber());
        Assert.assertTrue(createOrderWithoutAuthorizationResponse.isSuccess());
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkCreateOrderWithoutIngredientsResponseText(Response response) {
        Message message = response.body().as(Message.class);

        Assert.assertFalse(message.isSuccess());
        Assert.assertEquals("Ingredient ids must be provided", message.getMessage());
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkCreateOrderWrongIngredientResponseText(Response response) {

        Assert.assertTrue(response.getBody().asString().contains("Internal Server Error"));
    }

    @After
    public void deleteUser() {
        BaseApi baseApi = new BaseApi();

        baseApi.deleteUser(accessToken);
    }
}