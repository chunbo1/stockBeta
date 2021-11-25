package com.util;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DateUtilTest {
    @Before
    public void before() {

    }

    @SneakyThrows
    @Test
    public void getNextWorkingDayTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);
        String dateInString = "7/4/2021";//7/4/2021 is Sunday, 7/5 is set up as a holiday in yaml config
        String nextDateInString = "7/6/2021";
        Map<String, String> bankHolidays = new HashMap<>();
        bankHolidays.put("11/25/2021","2021ThanksGiving");
        bankHolidays.put("7/5/2021","2021IndependenceDay");
        Date date = formatter.parse(dateInString);
        Date nextDate = formatter.parse(nextDateInString);
        Date nextWorkingDay = DateTimeUtil.getNextWorkingDay(date, bankHolidays);
        assertThat(nextWorkingDay).isEqualTo(nextDate);
    }

    @SneakyThrows
    @Test
    public void isBankHolidayTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);
        Map<String, String> bankHolidays = new HashMap<>();
        bankHolidays.put("11/25/2021","2021ThanksGiving");

        String dateInString = "11/25/2021";
        Date date = formatter.parse(dateInString);
        boolean isHoliday = DateTimeUtil.isBankHoliday(date, bankHolidays);
        assertThat(isHoliday).isEqualTo(true);

        dateInString = "11/1/2021";//Monday
        date = formatter.parse(dateInString);
        isHoliday = DateTimeUtil.isBankHoliday(date, bankHolidays);
        assertThat(isHoliday).isEqualTo(false);
    }
}
