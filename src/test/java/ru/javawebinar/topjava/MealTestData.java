package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    private MealTestData() {
    }

    public static final Meal user_meal_1 = new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal user_meal_2 = new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal user_meal_3 = new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal user_meal_4 = new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal user_meal_5 = new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal user_meal_6 = new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal user_meal_7 = new Meal(START_SEQ + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final Meal admin_meal_1 = new Meal(START_SEQ + 10, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0, 0), "Админ ланч", 510);
    public static final Meal admin_meal_2 = new Meal(START_SEQ + 11, LocalDateTime.of(2015, Month.JUNE, 1, 20, 0, 0), "Админ ужин", 1500);

    public static final List<Meal> userMeals = Arrays.asList(
            user_meal_7,
            user_meal_6,
            user_meal_5,
            user_meal_4,
            user_meal_3,
            user_meal_2,
            user_meal_1
    );

    public static final List<Meal> adminMeals = Arrays.asList(
            admin_meal_2,
            admin_meal_1
    );

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2022, Month.MARCH, 5, 10, 0, 0), "new meal", 100);
    }

    public static Meal getUpdated() {
        Meal updatedMeal = new Meal(user_meal_1);
        updatedMeal.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 1));
        updatedMeal.setDescription("updated meal");
        updatedMeal.setCalories(501);
        return updatedMeal;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
