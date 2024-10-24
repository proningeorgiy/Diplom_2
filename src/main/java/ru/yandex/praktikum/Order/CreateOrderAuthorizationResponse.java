package ru.yandex.praktikum.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderAuthorizationResponse {
    private boolean success;
    private String name;
    private OrderAuthorization order;
}
