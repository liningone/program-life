package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.util.DateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MonthCalendar implements AIPCalendar {
    //每月的几号
    private Integer expectDayOfMonth = -1;

    public MonthCalendar(int expectDayOfMonth) {
        this.expectDayOfMonth = expectDayOfMonth;
    }

    public void setExpectDayOfMonth(int expectDayOfMonth) {
        this.expectDayOfMonth = expectDayOfMonth;
    }

    @Override
    public List<String> getExpectDayTime(String startTime, String endTime) {
        List<String> res = new ArrayList<>();

        LocalDate startDate = DateUtils.parseLocalDate(startTime);
        LocalDate endDate = DateUtils.parseLocalDate(endTime);

        LocalDate currentDate = startDate;
        currentDate = currentDate.withDayOfMonth(expectDayOfMonth);
        if (currentDate.isBefore(startDate)) {
            currentDate = currentDate.plusMonths(1);
        }

        while ((currentDate.isAfter(startDate) || currentDate.equals(startDate)) &&
                (currentDate.isBefore(endDate) || currentDate.equals(endDate))) {
            // 输出每个月的第二天日期
            res.add(DateUtils.parseDateStr(currentDate, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT));

            // 下一个月
            currentDate = currentDate.plusMonths(1);
        }

        return res;
    }
}
