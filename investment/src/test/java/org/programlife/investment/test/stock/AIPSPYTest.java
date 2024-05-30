package org.programlife.investment.test.stock;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.junit.Test;
import org.programlife.investment.stock.aip.InvestmentPeriodicity;
import org.programlife.investment.stock.aip.calendar.MonthCalendar;
import org.programlife.investment.stock.aip.v1.AIPOptionsV1;
import org.programlife.investment.stock.aip.v1.AIPYieldCalculatorV1;
import org.programlife.investment.stock.aip.v1.AIPYieldStatementV1;
import org.programlife.investment.stock.data.StockDataService;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AIPSPYTest {

    @Test
    public void test() {
        //定投间隔1个月，时长12个月，滚动的
        StockDataService dataService = new LocalDataService();
        AIPYieldCalculatorV1 calculatorV1 = new AIPYieldCalculatorV1(dataService);

        MonthCalendar monthCalendar = new MonthCalendar(1);
        List<String> dates = monthCalendar.getExpectDayTime("2010-01-01", "2023-01-01");

        List<Double> list = new ArrayList<>();

        for(String dateStr : dates) {
            String startTime = DateUtils.completeTime(dateStr);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            LocalDate oneYearLater = date.plusYears(1);
            String endTime = DateUtils.completeTime(oneYearLater.format(formatter));

            AIPOptionsV1 options = new AIPOptionsV1();
            options.setStockSymbol("SPY");
            options.setSingleAmount(1000);
            options.setPeriodicity(InvestmentPeriodicity.MONTHLY);
            options.setPeriodicityParameter(1);
            options.setStartTime(startTime);
            options.setEndTime(endTime);

            AIPYieldStatementV1 res = (AIPYieldStatementV1) calculatorV1.calculate(options);
            list.add(res.currentYield.profit);

            /*
            System.out.println(String.format("[%s, %s] = %s, %s", startTime, endTime,
                    res.currentYield.holdingYield, res.currentYield.holdingProfit));
             */
        }

        Percentile percentile = new Percentile();
        double[] array = list.stream().mapToDouble(Double::doubleValue).toArray();

        int percentile0 = (int) percentile.evaluate(array, 0.1);
        int percentile25 = (int) percentile.evaluate(array, 25);
        int percentile50 = (int) percentile.evaluate(array, 50);
        int percentile75 = (int) percentile.evaluate(array, 75);
        int percentile100 = (int) percentile.evaluate(array, 100);

        System.out.println("percentile0: " + percentile0);
        System.out.println("percentile25: " + percentile25);
        System.out.println("percentile50: " + percentile50);
        System.out.println("percentile75: " + percentile75);
        System.out.println("percentile100: " + percentile100);
    }
}
