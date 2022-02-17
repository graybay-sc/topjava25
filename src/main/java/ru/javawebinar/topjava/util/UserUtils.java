package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

public class UserUtils {
    private UserUtils() {
    }

    public static int USER_ID = 1;
    public static int ADMIN_ID = 2;

    public static User user = new User(USER_ID, "user", "user@gmail.com", "123", Role.USER);
    public static User admin = new User(ADMIN_ID, "admin", "admin@gmail.com", "admin", Role.USER);
}
