package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.createTo;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public MealTo get(int id) {
        log.info("get {}", id);
        return createTo(service.get(id, authUserId()), false);
    }

    public List<MealTo> getByDateTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getFiltered {} {} {} {}", startDate, startTime, endDate, endTime);
        return getTos(service.getFiltered(authUserId(),
                        startDate != null ? startDate : LocalDate.MIN,
                        startTime != null ? startTime : LocalTime.MIN,
                        endDate != null ? endDate : LocalDate.MAX,
                        endTime != null ? endTime : LocalTime.MAX),
                authUserCaloriesPerDay());
    }

    public MealTo create(MealTo mealTo) {
        log.info("create {}", mealTo);
        return createTo(service.create(MealsUtil.createFromTo(mealTo), authUserId()), false);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public void update(MealTo mealTo, int id) {
        log.info("update {} with id={}", mealTo, id);
//        assureIdConsistent(mealTo, id);
        service.update(MealsUtil.createFromTo(mealTo), authUserId());
    }
}