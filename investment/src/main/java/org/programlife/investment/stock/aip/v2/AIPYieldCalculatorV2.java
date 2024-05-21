package org.programlife.investment.stock.aip.v2;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.aip.AIPYieldCalculator;
import org.programlife.investment.stock.aip.AIPYieldStatement;
import org.programlife.investment.stock.aip.AIPOptions;
import org.programlife.investment.stock.aip.InvestmentPeriodicity;
import org.programlife.investment.stock.aip.calendar.AIPCalendar;
import org.programlife.investment.stock.aip.calendar.DayCalendar;
import org.programlife.investment.stock.aip.calendar.MonthCalendar;
import org.programlife.investment.stock.aip.calendar.WeekCalendar;
import org.programlife.investment.stock.aip.v1.*;
import org.programlife.investment.stock.calculation.YieldData;
import org.programlife.investment.stock.calculation.InvestmentCalculatorV1;
import org.programlife.investment.stock.calculation.Operation;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.KLineDataUtils;
import org.programlife.investment.stock.data.StockDataService;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/*
v2版本改动：
支持更详细的定投结果
 */
public class AIPYieldCalculatorV2 implements AIPYieldCalculator {
    private StockDataService dataService;

    private List<KLineData> stockDatas;

    @Override
    public AIPYieldStatement calculate(AIPOptions options) {
        //TODO dataService初始化不应该在这里
        if (this.dataService == null) {
            this.dataService = new LocalDataService();
        }

        AIPOptionsV1 optionsV1 = (AIPOptionsV1) options;
        String startTime = optionsV1.getStartTime();
        String endTime = optionsV1.getEndTime();
        this.stockDatas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

        List<Operation> operations = new ArrayList<>();
        List<String> dates = this.getExpectDayTime(startTime, endTime,
                optionsV1.getPeriodicity(), optionsV1.getPeriodicityParameter());
        for (String date : dates) {
            date = getRecentTradingDay(date);
            KLineData kLineData = KLineDataUtils.search(this.stockDatas, date);
            Operation operation = new Operation();
            operation.date = getRecentTradingDay(date);
            operation.principal = optionsV1.getSingleAmount();
            operation.price = kLineData.getClose();
        }

        InvestmentCalculatorV1 calculator = new InvestmentCalculatorV1();
        List<YieldData> yieldData = calculator.calculateEachInvestment(operations);

        AIPYieldStatementV1 res = new AIPYieldStatementV1();

        return res;
    }

    private List<String> getExpectDayTime(String startTime, String endTime,
                                          InvestmentPeriodicity periodicity, Integer periodicityParameter) {
        AIPCalendar calendar = null;
        if (periodicity == InvestmentPeriodicity.MONTHLY) {
            calendar = new MonthCalendar(periodicityParameter);
        } else if (periodicity == InvestmentPeriodicity.WEEKLY) {
            calendar = new WeekCalendar(periodicityParameter);
        } else {
            calendar = new DayCalendar();
        }

        return calendar.getExpectDayTime(startTime, endTime);
    }

    /*
    每个市场的交易日并不相同
    可以从k线图数据中判断是否是交易日
     */
    private boolean isTradeDay(String time) {
        return KLineDataUtils.search(stockDatas, time) != null;
    }

    /*
    如果目标日期是交易日，直接返回
    如果目标日期是非交易日，顺延到下个交易日
    例如 2024-05-02 -> 2024-05-06
     */
    private String getRecentTradingDay(String date) {
        boolean isTradeDay = isTradeDay(date);
        if(isTradeDay) {
            return date;
        }

        int idx = KLineDataUtils.searchNearestIndex(stockDatas, date, true);
        return stockDatas.get(idx).getTime();
    }

    @Test
    public void testGetRecentTradingDay() {
        String date = "2024-05-02";
        StockDataService dataService = new LocalDataService();
        String startTime = DateUtils.completeTime("2024-04-30");
        String endTime = DateUtils.completeTime("2024-05-10");
        this.stockDatas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

        Assert.assertEquals("2024-05-06", getRecentTradingDay(date));
    }

    @Test
    public void testWeekly() {
        /*
        生成每个周1号的时间
        2024-04-01
        2024-04-08
         */
        StockDataService dataService = new LocalDataService();
        String startTime = DateUtils.completeTime("2024-04-10");
        String endTime = DateUtils.completeTime("2024-05-10");
        this.stockDatas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

        AIPCalendar calendar = new WeekCalendar(4);
        List<String> dates = calendar.getExpectDayTime(startTime, endTime);
        for (String date : dates) {
            System.out.println(getRecentTradingDay(date));
        }
    }

    @Test
    public void testMonthly() {
        /*
        生成每个月1号的时间
        2024-01-01
        2024-02-01
        2024-03-01
         */
        StockDataService dataService = new LocalDataService();
        //String startTime = DateUtils.completeTime("2023-05-12");
        String startTime = DateUtils.completeTime("2023-04-10");
        String endTime = DateUtils.completeTime("2024-05-10");
        this.stockDatas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

    }
}



