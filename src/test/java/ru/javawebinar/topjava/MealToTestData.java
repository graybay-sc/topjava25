package ru.javawebinar.topjava;

import ru.javawebinar.topjava.to.MealTo;

public class MealToTestData {
    private MealToTestData() {
    }

    public static final MatcherFactory.Matcher<MealTo> MEAL_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MealTo.class, "user");
}
