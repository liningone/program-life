package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.util.DateUtils;

import java.util.ArrayList;
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
        this.expectDayOfWeek = expectDayOfWeek;
    }

    public void setExpectDayOfWeek(int expectDayOfWeek) {
        this.expectDayOfWeek = expectDayOfWeek;
    }

    @Override
    public List<String> generate(String startTime, String endTime) {
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
}
