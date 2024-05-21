package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DayCalendar implements AIPCalendar {

    @Override
    public List<String> getExpectDayTime(String startTime, String endTime) {
        long mills = DateUtils.parseMills(startTime);

        List<String> res = new ArrayList<>();
        long endMills = DateUtils.parseMills(endTime);
        while (mills < endMills) {
            String date = DateUtils.parseDateStr(mills, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);
            res.add(date);
            mills += TimeUnit.DAYS.toMillis(1);
        }

        return res;
    }
}
