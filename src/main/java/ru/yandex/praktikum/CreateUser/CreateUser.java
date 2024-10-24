package ru.yandex.praktikum.CreateUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUser {

    private String email;
    private String password;
    private String name;

}
