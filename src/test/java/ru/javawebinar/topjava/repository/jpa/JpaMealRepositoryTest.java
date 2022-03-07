package ru.javawebinar.topjava.repository.jpa;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.StopWatchTestRule;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JpaMealRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(StopWatchTestRule.class);

    @Autowired
    private MealRepository repository;

    private static final Map<String, Long> testsDuration = new HashMap<>();

    @Rule
    public final TestRule stopwatch = new StopWatchTestRule(testsDuration);

    @AfterClass
    public static void afterAll() {
        log.info("--- TESTS DURATION ---");
        long totalTime = 0;
        for (Map.Entry<String, Long> entry : testsDuration.entrySet()) {
            Long duration = entry.getValue();
            log.info("Test {} duration: {} ms", entry.getKey(), entry.getValue());
            totalTime += duration;
        }
        log.info("TOTAL TIME: {} ms", totalTime);
    }

    @Test
    public void saveNew() {
        Meal created = repository.save(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(repository.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                repository.save(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        repository.save(updated, USER_ID);
        MEAL_MATCHER.assertMatch(repository.get(MEAL1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertNull(repository.save(meal1, ADMIN_ID));
        MEAL_MATCHER.assertMatch(repository.get(MEAL1_ID, USER_ID), meal1);
    }

    @Test
    public void delete() {
        repository.delete(MEAL1_ID, USER_ID);
        assertNull(repository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertFalse(repository.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertFalse(repository.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void get() {
        Meal actual = repository.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
    }

    @Test
    public void getNotFound() {
        assertNull(repository.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertNull(repository.get(MEAL1_ID, ADMIN_ID));
    }


    @Test
    public void getAll() {
        MEAL_MATCHER.assertMatch(repository.getAll(USER_ID), meals);
    }

    @Test
    public void getBetweenHalfOpen() {
        MEAL_MATCHER.assertMatch(repository.getBetweenHalfOpen(
                        LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), USER_ID),
                meal2, meal1);
    }
}