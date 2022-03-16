package ru.javawebinar.topjava.util;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.lang.Nullable;

public class Util {
    private Util() {
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }

    public static boolean isNotJdbcProfile(Environment environment) {
        return environment.acceptsProfiles(Profiles.of("!" + ru.javawebinar.topjava.Profiles.JDBC));
    }
}