package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ru.yandex.praktikum.CreateUser.CreateUser;
import ru.yandex.praktikum.GetOrder.OrdersResponse;
import ru.yandex.praktikum.LoginUser.LoginUser;
import ru.yandex.praktikum.Order.CreateOrder;

import java.util.Arrays;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GetOrdersTest {

    private static final String USEREMAIL = "testuser12345@yandex.ru";
    private static final String USERPASSWORD = "12346";
    private static final String USERNAME = "Vasja";

    String accessToken = "";

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    @Description("Проверка кода ответа и сообщения ответа при получении ответа авторизованным пользователем")
    public void getOrdersAuthorizationTest() {

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

        //Получение списка зказов
        Response getOrdersResponse;
        getOrdersResponse = getOrdersAuthorizationWithResponse();

        //Проверка на правильный код ответа
        checkResponseCode(getOrdersResponse, SC_OK);
        //Проверка на правильный текст ответа
        checkGetOrdersAuthorizationResponseText(getOrdersResponse);

    }

    @Test
    @DisplayName("Попытка получения списка зказов неавторизованного пользователя")
    @Description("Проверка кода ответа и сообщения ответа при попытке получения списка заказов неавторизованного пользователя")
    public void getOrdersWithoutAuthorizationTest() {

        //Получение списка зказов
        Response getOrdersResponse;
        getOrdersResponse = getOrdersWithoutAuthorizationWithResponse();

        //Проверка на правильный код ответа
        checkResponseCode(getOrdersResponse, SC_UNAUTHORIZED);
        //Проверка на правильный текст ответа
        checkGetOrdersWithoutAuthorizationResponseText(getOrdersResponse);
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
    public Response createOrderAuthorizationWithResponse(CreateOrder createOrder) {
        CreateOrderApi createOrderApi = new CreateOrderApi(createOrder);

        Response response;
        response = createOrderApi.createOrderAuthorization(accessToken);

        createOrderApi.showCreateOrderResponseData(response);

        return response;
    }

    @Step("Получение списка зказов")
    public Response getOrdersAuthorizationWithResponse() {
        GetOrdersApi getOrdersApi = new GetOrdersApi();

        Response response;
        response = getOrdersApi.getOrdersAuthorization(accessToken);

        getOrdersApi.showGetOrdersResponseData(response);

        return response;
    }

    @Step("Получение списка зказов")
    public Response getOrdersWithoutAuthorizationWithResponse() {
        GetOrdersApi getOrdersApi = new GetOrdersApi();

        Response response;
        response = getOrdersApi.getOrdersWithoutAuthorization();

        getOrdersApi.showGetOrdersResponseData(response);

        return response;
    }

    @Step("Проверка на правильный код ответа")
    public void checkResponseCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkGetOrdersAuthorizationResponseText(Response response) {
        OrdersResponse ordersResponse = response.body().as(OrdersResponse.class);

        Assert.assertTrue(ordersResponse.isSuccess());
        Assert.assertEquals(1, ordersResponse.getOrders().size());
        Assert.assertEquals(1, ordersResponse.getTotal());
        Assert.assertEquals(1, ordersResponse.getTotalToday());
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkGetOrdersWithoutAuthorizationResponseText(Response response) {
        Message message = response.body().as(Message.class);

        Assert.assertFalse(message.isSuccess());
        Assert.assertEquals("You should be authorised", message.getMessage());
    }

    @After
    public void deleteUser() {
        BaseApi baseApi = new BaseApi();

        baseApi.deleteUser(accessToken);
    }
}