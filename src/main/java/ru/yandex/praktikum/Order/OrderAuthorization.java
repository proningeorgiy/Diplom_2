package ru.yandex.praktikum.Order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderAuthorization {
    private List<Ingredient> ingredients;
    private String _id;
    private Owner owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
    private int price;
}
