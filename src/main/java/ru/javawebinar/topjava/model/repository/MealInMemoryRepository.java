package ru.javawebinar.topjava.model.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealInMemoryRepository implements Repository<Meal> {

    private final AtomicInteger sequence = new AtomicInteger(0);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    public MealInMemoryRepository() {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public void save(Meal newMeal) {
        int newId = sequence.incrementAndGet();
        meals.put(newId, new Meal(newId, newMeal.getDateTime(), newMeal.getDescription(), newMeal.getCalories()));
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public void update(int id, Meal meal) {
        meals.put(id, new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories()));
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
