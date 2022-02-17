package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.userMeals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        Meal updatedMeal = repository.computeIfPresent(meal.getId(),
                (id, oldMeal) -> oldMeal.getUserId() == userId ? meal : oldMeal);
        return meal.equals(updatedMeal) ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.computeIfPresent(id, (k, oldMeal) -> oldMeal.getUserId() == userId ? null : oldMeal) == null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfClosed(meal.getDate(), startDate.toLocalDate(), endDate.toLocalDate()))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startDate.toLocalTime(), endDate.toLocalTime()))
                .collect(Collectors.toList());
    }
}
