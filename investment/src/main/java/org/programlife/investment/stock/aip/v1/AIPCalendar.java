package org.programlife.investment.stock.aip.v1;

import java.util.List;

public interface AIPCalendar {
    List<String> generate(String startTime, String endTime);
}
