package ru.yandex.praktikum.GetOrder;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrdersResponse {
    private boolean success;
    private List<Order> orders;
    private int total;
    private int totalToday;
}
