package com.afterpay.frauddetector.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeUtilTest {

    @Test
    void testTimeDifferenceBetweenTwoLocalDateTimes() {
        //given
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusSeconds(10);

        //when
        long difference = DateTimeUtil.getTimeDifference(ChronoUnit.SECONDS, time1, time2);

        //then
        assertEquals(difference, 10);
    }

    @Test
    void testConversionOfDateToLocalDateTime() {
        //given
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";

        //when
        LocalDateTime parsedDateTime = DateTimeUtil.parse("2021-01-04T22:24:11", pattern);

        //then
        assertEquals("2021-01-04T22:24:11", parsedDateTime.toString());
    }
}