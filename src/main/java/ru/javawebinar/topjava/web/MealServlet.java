package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.repository.MealInMemoryRepository;
import ru.javawebinar.topjava.model.repository.Repository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MealServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final Repository<Meal> mealRepository = new MealInMemoryRepository();
    private static final int MAX_CALORIES_PER_DAY = 2000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("meals", MealsUtil.toMealTo(mealRepository.getAll(), MAX_CALORIES_PER_DAY));
            log.debug("forward to meals");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            mealRepository.delete(mealId);
            log.debug("redirect to meals");
            response.sendRedirect("meals");
        } else if (action.equalsIgnoreCase("update")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            log.debug("forward to meal");
            request.setAttribute("meal", MealsUtil.createTo(mealRepository.getById(mealId), false));
            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("add")) {
            log.debug("forward to meal");
            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        } else {
            request.setAttribute("meals", mealRepository.getAll());
            log.debug("forward to meals");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(request.getParameter("date"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException ignored) {
        }
        if (dateTime != null) {
            String idParameter = request.getParameter("id");
            int calories = Integer.parseInt(request.getParameter("calories"));
            if (idParameter.isEmpty()) {
                Meal newMeal = new Meal(null, dateTime, request.getParameter("description"), calories);
                mealRepository.save(newMeal);
            } else {
                int id = Integer.parseInt(request.getParameter("id"));
                Meal newMeal = new Meal(id, dateTime, request.getParameter("description"), calories);
                mealRepository.update(id, newMeal);
            }
        }
        log.debug("redirect to meals");
        response.sendRedirect("meals");
    }
}
