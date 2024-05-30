package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.aip.AIPYieldStatement;
import org.programlife.investment.stock.aip.calendar.MonthCalendar;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AIPUtils {
    public static List<AIPYieldStatement> batchCalculate(String stockSymbol, String startTime, String endTime,
                                                         int amount, int monthNum, AIPOptionsV1 template) {
        List<AIPYieldStatement> result = new ArrayList<>();

        LocalDate end = DateUtils.parseLocalDate(endTime, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);
        end = end.minusMonths(monthNum);
        endTime = DateUtils.parseDateStr(end, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);

        MonthCalendar monthCalendar = new MonthCalendar(1);
        List<String> dates = monthCalendar.getExpectDayTime(startTime, endTime);

        AIPYieldCalculatorV1 calculatorV1 = new AIPYieldCalculatorV1(new LocalDataService());
        for(String dateStr : dates) {
            String aipStartTime = DateUtils.completeTime(dateStr);

            LocalDate localDate = DateUtils.parseLocalDate(dateStr, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);
            localDate = localDate.plusMonths(monthNum).minusDays(1);
            String aipEndTime = DateUtils.completeTime(DateUtils.parseDateStr(localDate, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT));

            AIPOptionsV1 options = new AIPOptionsV1();
            options.setStockSymbol(stockSymbol);
            options.setSingleAmount(amount / monthNum);
            options.setPeriodicity(template.getPeriodicity());
            options.setPeriodicityParameter(template.getPeriodicityParameter());
            options.setStartTime(aipStartTime);
            options.setEndTime(aipEndTime);

            AIPYieldStatementV1 res = (AIPYieldStatementV1) calculatorV1.calculate(options);
            result.add(res);
        }

        return result;
    }
}
