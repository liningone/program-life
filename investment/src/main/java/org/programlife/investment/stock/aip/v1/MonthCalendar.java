package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.util.DateUtils;

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
    public List<String> generate(String startTime, String endTime) {
        long mills = DateUtils.parseMills(startTime);
        int dayOfMonth = DateUtils.getDayOfMonth(startTime);

        //定位到起始时间这月的第一日
        mills -= TimeUnit.DAYS.toMillis(dayOfMonth - 1);

        //本月不能购买，跳到下一月
        if (dayOfMonth > expectDayOfMonth) {
            mills = DateUtils.plusMonths(mills, 1);
        }

        //定位到目标日
        mills += TimeUnit.DAYS.toMillis(expectDayOfMonth - 1);

        List<String> res = new ArrayList<>();
        long endMills = DateUtils.parseMills(endTime);
        while (mills < endMills) {
            String date = DateUtils.parseDateStr(mills, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);
            res.add(date);
            mills = DateUtils.plusMonths(mills, 1);
        }

        return res;
    }
}
