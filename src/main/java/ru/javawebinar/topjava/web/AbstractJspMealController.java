package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public abstract class AbstractJspMealController {

    protected final MealRestController controller;

    public AbstractJspMealController(MealRestController controller) {
        this.controller = controller;
    }

    protected int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
