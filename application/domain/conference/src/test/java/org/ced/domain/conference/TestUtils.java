package org.ced.domain.conference;

import java.util.Calendar;
import java.util.Date;

public final class TestUtils {
    
    private TestUtils() {}

    public static Date toDate(int year, int month, int day) {
        return toDate(year, month, day, 0, 0, 0);
    }

    public static Date toDate(int year, int month, int day, int hour, int min) {
        return toDate(year, month, day, hour, min, 0);
    }

    public static Date toDate(int year, int month, int day, int hour, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day, hour, min, sec);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
