package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.util.DateUtils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
计算一段时间范围内每一周的星期x的日期
例如星期四
    2024-05-02
    2024-05-09
 */
public class WeekCalendar implements AIPCalendar {
    //每周的星期几
    private Integer expectDayOfWeek = null;

    public WeekCalendar(int expectDayOfWeek) {
        this.setExpectDayOfWeek(expectDayOfWeek);
    }

    public void setExpectDayOfWeek(int expectDayOfWeek) {
        checkDayOfWeek(expectDayOfWeek);
        this.expectDayOfWeek = expectDayOfWeek;
    }

    @Override
    public List<String> getExpectDayTime(String startTime, String endTime) {
        List<String> res = new ArrayList<>();

        LocalDate startDate = DateUtils.parseLocalDate(startTime);
        LocalDate endDate = DateUtils.parseLocalDate(endTime);

        DayOfWeek dayOfWeek = convert(this.expectDayOfWeek);
        LocalDate currentDate = startDate.with(TemporalAdjusters.nextOrSame(dayOfWeek));
        while ((currentDate.isAfter(startDate) || currentDate.equals(startDate)) &&
                (currentDate.isBefore(endDate) || currentDate.equals(endDate))) {
            res.add(DateUtils.parseDateStr(currentDate, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT));

            // 下一个星期
            currentDate = currentDate.with(TemporalAdjusters.next(dayOfWeek));
        }

        return res;
    }

    private DayOfWeek convert(int expectDayOfWeek) {
        switch (expectDayOfWeek) {
            case 1 : {
                return DayOfWeek.MONDAY;
            }
            case 2 : {
                return DayOfWeek.TUESDAY;
            }
            case 3 : {
                return DayOfWeek.WEDNESDAY;
            }
            case 4 : {
                return DayOfWeek.THURSDAY;
            }
            case 5 : {
                return DayOfWeek.FRIDAY;
            }
            case 6 : {
                return DayOfWeek.SATURDAY;
            }
            case 7 : {
                return DayOfWeek.SUNDAY;
            }
        }

        throw new RuntimeException("DayOfWeek error");
    }

    /*
    @Override
    public List<String> getExpectDayTime(String startTime, String endTime) {
        long mills = DateUtils.parseMills(startTime);
        int dayOfWeek = DateUtils.getDayOfWeek(startTime);

        //定位到起始时间这周的星期一
        mills -= TimeUnit.DAYS.toMillis(dayOfWeek - 1);

        //本周不能购买，跳到下一周
        if (dayOfWeek > expectDayOfWeek) {
            mills += TimeUnit.DAYS.toMillis(7);
        }

        //定位到目标星期
        mills += TimeUnit.DAYS.toMillis(expectDayOfWeek - 1);

        List<String> res = new ArrayList<>();
        long endMills = DateUtils.parseMills(endTime);
        while (mills < endMills) {
            String date = DateUtils.parseDateStr(mills, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);
            res.add(date);
            mills += TimeUnit.DAYS.toMillis(7);
        }

        return res;
    }
    */

    private void checkDayOfWeek(int expectDayOfWeek) {
        if (expectDayOfWeek >= 1 && expectDayOfWeek <= 7) {
            return;
        }

        throw new RuntimeException("dayOfWeek range is 1-7");
    }
}
