package org.programlife.investment.stock.aip.v1;

import java.util.List;

public interface AIPCalendar {
    List<String> getExpectDayTime(String startTime, String endTime);
}
