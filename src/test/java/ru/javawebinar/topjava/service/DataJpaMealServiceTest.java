package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.admin;

@ActiveProfiles("datajpa")
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        Meal expectedMeal = new Meal(adminMeal1);
        expectedMeal.setUser(admin);
        assertThat(actual).usingRecursiveComparison().ignoringFields("user.roles", "user.meals", "user.registered").isEqualTo(expectedMeal);
    }
}
