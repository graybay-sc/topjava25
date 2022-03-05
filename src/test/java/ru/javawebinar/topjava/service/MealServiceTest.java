package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), UserTestData.USER_ID);
        int newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, UserTestData.USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(
                                null,
                                LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                                "Duplicate", 10),
                        UserTestData.USER_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(user_meal_1.getId(), UserTestData.USER_ID);
        assertMatch(meal, user_meal_1);
    }

    @Test
    public void getAnotherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.get(user_meal_1.getId(), UserTestData.ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> actualUserMeals = service.getAll(UserTestData.USER_ID);
        assertMatch(actualUserMeals, userMeals);
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> actualMeals = service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30),
                UserTestData.USER_ID);
        assertMatch(actualMeals, Arrays.asList(user_meal_3, user_meal_2, user_meal_1));
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdated();
        Integer updatedMealId = updatedMeal.getId();
        service.update(updatedMeal, UserTestData.USER_ID);
        assertMatch(service.get(updatedMealId, UserTestData.USER_ID), getUpdated());
    }

    @Test
    public void updateAnotherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), UserTestData.ADMIN_ID));
    }

    @Test()
    public void delete() {
        int user_meal_Id = user_meal_1.getId();
        service.delete(user_meal_Id, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(user_meal_Id, UserTestData.USER_ID));
    }

    @Test()
    public void deleteAnotherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(user_meal_1.getId(), UserTestData.ADMIN_ID));
    }
}
