package ru.yandex.praktikum.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderWithoutAuthorizationResponse {

    private String name;
    private OrderWithoutAuthorization order;
    private boolean success;

}
