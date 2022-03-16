package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Controller
public class JspMealFormController extends AbstractJspMealController {

    public JspMealFormController(MealRestController controller) {
        super(controller);
    }

    @PostMapping("/meals")
    public String saveMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            controller.update(meal, getId(request));
        } else {
            controller.create(meal);
        }
        return "redirect:meals";
    }
}
