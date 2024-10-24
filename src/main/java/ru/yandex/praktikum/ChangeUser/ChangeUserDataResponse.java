package ru.yandex.praktikum.ChangeUser;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.praktikum.User;

@Getter
@Setter
public class ChangeUserDataResponse {

    private boolean success;
    private User user;

}
