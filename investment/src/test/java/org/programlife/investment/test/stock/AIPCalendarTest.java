package org.programlife.investment.test.stock;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.aip.calendar.AIPCalendar;
import org.programlife.investment.stock.aip.calendar.DayCalendar;
import org.programlife.investment.stock.aip.calendar.MonthCalendar;
import org.programlife.investment.stock.aip.calendar.WeekCalendar;

import java.util.List;

public class AIPCalendarTest {

    @Test
    public void testWeekCalendar() {
        AIPCalendar calendar = new WeekCalendar(2);
        List<String> dates = calendar.getExpectDayTime("2024-05-01", "2024-05-30");
        Assert.assertEquals(4, dates.size());
        Assert.assertEquals("2024-05-07", dates.get(0));
        Assert.assertEquals("2024-05-14", dates.get(1));
        Assert.assertEquals("2024-05-21", dates.get(2));
        Assert.assertEquals("2024-05-28", dates.get(3));

        ((WeekCalendar) calendar).setExpectDayOfWeek(4);
        dates = calendar.getExpectDayTime("2024-05-01", "2024-05-30");
        Assert.assertEquals(5, dates.size());
        Assert.assertEquals("2024-05-02", dates.get(0));
        Assert.assertEquals("2024-05-09", dates.get(1));
        Assert.assertEquals("2024-05-16", dates.get(2));
        Assert.assertEquals("2024-05-23", dates.get(3));
        Assert.assertEquals("2024-05-30", dates.get(4));

        ((WeekCalendar) calendar).setExpectDayOfWeek(4);
        dates = calendar.getExpectDayTime("2024-04-10", "2024-05-10");
        Assert.assertEquals(5, dates.size());
    }

    @Test
    public void testMonthCalendar() {
        AIPCalendar calendar = new MonthCalendar(12);
        List<String> dates = calendar.getExpectDayTime("2023-05-01", "2024-05-30");
        Assert.assertEquals(13, dates.size());

        int i = 0;
        Assert.assertEquals("2023-05-12", dates.get(i ++));
        Assert.assertEquals("2023-06-12", dates.get(i ++));
        Assert.assertEquals("2023-07-12", dates.get(i ++));
        Assert.assertEquals("2023-08-12", dates.get(i ++));
        Assert.assertEquals("2023-09-12", dates.get(i ++));
        Assert.assertEquals("2023-10-12", dates.get(i ++));
        Assert.assertEquals("2023-11-12", dates.get(i ++));
        Assert.assertEquals("2023-12-12", dates.get(i ++));
        Assert.assertEquals("2024-01-12", dates.get(i ++));
        Assert.assertEquals("2024-02-12", dates.get(i ++));
        Assert.assertEquals("2024-03-12", dates.get(i ++));
        Assert.assertEquals("2024-04-12", dates.get(i ++));
        Assert.assertEquals("2024-05-12", dates.get(i ++));

        ((MonthCalendar) calendar).setExpectDayOfMonth(4);
        dates = calendar.getExpectDayTime("2023-05-05", "2023-08-30");
        Assert.assertEquals(3, dates.size());
        i = 0;
        Assert.assertEquals("2023-06-04", dates.get(i ++));
        Assert.assertEquals("2023-07-04", dates.get(i ++));
        Assert.assertEquals("2023-08-04", dates.get(i ++));
    }

    @Test
    public void testDayCalendar() {
        AIPCalendar calendar = new DayCalendar();
        List<String> dates = calendar.getExpectDayTime("2023-05-01", "2024-05-30");
        Assert.assertEquals(395, dates.size());

        Assert.assertEquals("2023-05-01", dates.get(0));
        Assert.assertEquals("2024-05-29", dates.get(394));
    }
}
