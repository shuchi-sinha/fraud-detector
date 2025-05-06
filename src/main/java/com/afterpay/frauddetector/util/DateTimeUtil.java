package com.afterpay.frauddetector.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * This class provided utility methods related to date and time.
 *
 * @author shuchi
 */
public class DateTimeUtil {

    private DateTimeUtil() {
        // Added to avoid object instantiation
    }

    /**
     * Method to get the time difference based on the chrono unit provided.
     *
     * @param chronoUnit  the chrono unit
     * @param initalTime  the initial time
     * @param currentTime the current time
     * @return the time difference value in chrono unit
     */
    public static long getTimeDifference(final ChronoUnit chronoUnit, final LocalDateTime initalTime, final LocalDateTime currentTime) {
        return Math.abs(chronoUnit.between(initalTime, currentTime));
    }

    public static LocalDateTime parse(final String date, final String pattern) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(date, formatter);
    }
}
