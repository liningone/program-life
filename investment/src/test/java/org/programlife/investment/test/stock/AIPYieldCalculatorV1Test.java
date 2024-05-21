package org.programlife.investment.test.stock;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.aip.v1.AIPOptionsV1;
import org.programlife.investment.stock.aip.v1.AIPYieldCalculatorV1;
import org.programlife.investment.stock.aip.v1.AIPYieldStatementV1;
import org.programlife.investment.stock.aip.v1.InvestmentPeriodicity;
import org.programlife.investment.stock.calculation.YieldData;
import org.programlife.investment.stock.util.DateUtils;

import java.util.Collections;
import java.util.List;

public class AIPYieldCalculatorV1Test {
    @Test
    public void testMonthly() {
        AIPYieldCalculatorV1 calculatorV1 = new AIPYieldCalculatorV1();
        String startTime = DateUtils.completeTime("2023-04-30");
        String endTime = DateUtils.completeTime("2024-05-10");

        AIPOptionsV1 options = new AIPOptionsV1();
        options.setSingleAmount(1000);
        options.setPeriodicity(InvestmentPeriodicity.MONTHLY);
        options.setPeriodicityParameter(12);
        options.setStartTime(startTime);
        options.setEndTime(endTime);

        AIPYieldStatementV1 res = (AIPYieldStatementV1) calculatorV1.calculate(options);

        List<YieldData> yieldDataList = res.yieldForEachInvestment;
        Collections.reverse(yieldDataList);

        Assert.assertEquals(12, yieldDataList.size());
        YieldData data = yieldDataList.get(0);
        Assert.assertEquals("2024-04-12", data.date);
        Assert.assertEquals(12000d, data.totalCost, 0d);
        Assert.assertEquals(11485, data.holdingAmount, 1d);
        Assert.assertEquals(-514, data.holdingProfit, 1d);
        Assert.assertEquals(-0.042f, data.holdingYield, 0.001f);
        Assert.assertEquals(3.3d, data.quota, 0.11d);
    }

    @Test
    public void testWeekly() {
        AIPYieldCalculatorV1 calculatorV1 = new AIPYieldCalculatorV1();
        String startTime = DateUtils.completeTime("2024-04-01");
        String endTime = DateUtils.completeTime("2024-05-10");

        AIPOptionsV1 options = new AIPOptionsV1();
        options.setSingleAmount(1000);
        options.setPeriodicity(InvestmentPeriodicity.WEEKLY);
        options.setPeriodicityParameter(2);
        options.setStartTime(startTime);
        options.setEndTime(endTime);

        AIPYieldStatementV1 res = (AIPYieldStatementV1) calculatorV1.calculate(options);

        List<YieldData> yieldDataList = res.yieldForEachInvestment;
        Collections.reverse(yieldDataList);

        Assert.assertEquals(6, yieldDataList.size());
        YieldData data = yieldDataList.get(0);
        Assert.assertEquals("2024-05-07", data.date);
        Assert.assertEquals(6000d, data.totalCost, 0d);
        Assert.assertEquals(6158, data.holdingAmount, 1d);
        Assert.assertEquals(158, data.holdingProfit, 1d);
        Assert.assertEquals(0.026f, data.holdingYield, 0.001f);
        Assert.assertEquals(1.6d, data.quota, 0.11d);
    }

    /*
    @Test
    public void testGetRecentTradingDay() {
        AIPYieldCalculatorV1 calculatorV1 = new AIPYieldCalculatorV1();
        String date = "2024-05-02";
        StockDataService dataService = new LocalDataService();
        String startTime = DateUtils.completeTime("2024-04-30");
        String endTime = DateUtils.completeTime("2024-05-10");

        Assert.assertEquals("2024-05-06", calculatorV1.getRecentTradingDay(date));
    }
     */
}
