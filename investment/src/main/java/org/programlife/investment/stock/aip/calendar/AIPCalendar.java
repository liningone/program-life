package org.programlife.investment.stock.aip.calendar;

import java.util.List;

public interface AIPCalendar {
    List<String> getExpectDayTime(String startTime, String endTime);
}
