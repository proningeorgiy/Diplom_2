package ru.yandex.praktikum.Order;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrder {

    List<String> ingredients;
}
