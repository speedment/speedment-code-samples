package com.speedment.example.spring.aggregate.util;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.text.NumberFormat.getCurrencyInstance;
import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class Utils {

    public static String fromEpochSecond(int date) {
        return '"' + ofEpochSecond((long) date, 0, UTC).format(DATES) + '"';
    }

    public static int toEpochSecond(String date) {
        return (int) LocalDate.from(DATES.parse(date))
            .atStartOfDay()
            .toInstant(UTC)
            .getEpochSecond();
    }

    public static String toCurrency(double value) {
        return '"' + CASH.format(value) + '"';
    }

    public static final DateTimeFormatter DATES = ofPattern("yyyy-MM-dd");
    public static final NumberFormat CASH = getCurrencyInstance(new Locale("en", "US"));

    private Utils() {}

}
