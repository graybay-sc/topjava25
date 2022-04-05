package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeFormatter implements Formatter<LocalTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return LocalTime.parse(text, formatter);
    }

    @Override
    public String print(LocalTime time, Locale locale) {
        if (time == null) {
            return "";
        }
        return formatter.format(time);
    }
}
