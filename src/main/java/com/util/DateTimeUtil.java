package com.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DateTimeUtil {
    public static Date getPreviousWorkingDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int dayOfWeek;
        do {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        } while (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY );

            return cal.getTime();
    }

    public static Date getNextWorkingDay(Date date, Map<String, String> bankHolidays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String dString = "";
        int dayOfWeek;
        do {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            dString = dateFormat.format(cal.getTime());
        } while (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || bankHolidays.containsKey(dString));

        return cal.getTime();
    }

    public static Boolean isBankHoliday(Date date, Map<String, String> bankHolidays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String dString = dateFormat.format(cal.getTime());

        if((Calendar.SATURDAY == cal.get(cal.DAY_OF_WEEK)) || (Calendar.SUNDAY == cal.get(cal.DAY_OF_WEEK)) || bankHolidays.containsKey(dString)) {
            return (true);
        } else {
            return false;
        }

    }

    public static String getPastDateString(int offset) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        return dateFormat.format(cal.getTime());
    }



}
