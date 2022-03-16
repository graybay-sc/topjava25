package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractJspMealController {

    public JspMealController(MealRestController controller) {
        super(controller);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", controller.getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getFiltered(Model model,
                              @PathVariable Map<String, String> pathVars,
                              @RequestParam String startDate,
                              @RequestParam String endDate,
                              @RequestParam String startTime,
                              @RequestParam String endTime) {
        LocalDate startDateParsed = parseLocalDate(startDate);
        LocalDate endDateParsed = parseLocalDate(endDate);
        LocalTime startTimeParsed = parseLocalTime(startTime);
        LocalTime endTimeParsed = parseLocalTime(endTime);
        model.addAttribute("meals", controller.getBetween(startDateParsed, startTimeParsed, endDateParsed, endTimeParsed));
        return "meals";
    }

    @GetMapping("/new")
    public String create(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        model.addAttribute("create", true);
        return "mealForm";
    }

    @GetMapping("/{id}")
    public String update(Model model, @PathVariable String id) {
        final Meal meal = controller.get(Integer.parseInt(id));
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        controller.delete(Integer.parseInt(id));
        return "redirect:/meals";
    }
}
