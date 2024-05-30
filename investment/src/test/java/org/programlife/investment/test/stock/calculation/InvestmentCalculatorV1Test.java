package org.programlife.investment.test.stock.calculation;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.calculation.YieldData;
import org.programlife.investment.stock.calculation.InvestmentCalculatorV1;
import org.programlife.investment.stock.calculation.Operation;
import org.programlife.investment.stock.calculation.YieldDataUtils;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvestmentCalculatorV1Test {
    @Test
    public void testCalculate() {
        LocalDataService dataService = new LocalDataService();
        InvestmentCalculatorV1 calculatorV1 = new InvestmentCalculatorV1();

        List<String> times = new ArrayList<>();
        times.add(DateUtils.completeTime("2023-05-12"));
        times.add(DateUtils.completeTime("2023-06-12"));
        times.add(DateUtils.completeTime("2023-07-12"));
        times.add(DateUtils.completeTime("2023-08-14"));
        times.add(DateUtils.completeTime("2023-09-12"));
        times.add(DateUtils.completeTime("2023-10-12"));
        times.add(DateUtils.completeTime("2023-11-13"));
        times.add(DateUtils.completeTime("2023-12-12"));
        times.add(DateUtils.completeTime("2024-01-12"));
        times.add(DateUtils.completeTime("2024-02-19"));
        times.add(DateUtils.completeTime("2024-03-12"));
        times.add(DateUtils.completeTime("2024-04-12"));

        List<KLineData> datas = dataService.queryKLineData("000300.SH", 240, 0, times);

        List<Operation> operationList = new ArrayList<>();
        for (KLineData data : datas) {
            Operation operation = new Operation();
            operation.price = data.getClose();
            operation.principal = 1000;
            operation.date = data.getTime();
            operationList.add(operation);
        }

        datas = dataService.queryKLineData("000300.SH", 240, 0,
                Collections.singletonList(DateUtils.completeTime("2024-05-10")));

        YieldData summary = calculatorV1.calculate(datas.get(0), operationList);
        Assert.assertEquals("2024-05-10", summary.date);
        Assert.assertEquals(12000d, summary.totalCost, 0d);
        Assert.assertEquals(12116, summary.finalAmount, 1d);
        Assert.assertEquals(116, summary.profit, 1d);
        Assert.assertEquals(0.009f, summary.returnRate, 0.001f);
        Assert.assertEquals(3.3d, summary.quota, 0.11d);
        Assert.assertEquals(0.018d, summary.annualIRR, 0.01d);
    }

    @Test
    public void testCalculateEachInvestment() {
        LocalDataService dataService = new LocalDataService();
        InvestmentCalculatorV1 calculatorV1 = new InvestmentCalculatorV1();

        List<String> times = new ArrayList<>();
        times.add(DateUtils.completeTime("2023-05-12"));
        times.add(DateUtils.completeTime("2023-06-12"));
        times.add(DateUtils.completeTime("2023-07-12"));
        times.add(DateUtils.completeTime("2023-08-14"));
        times.add(DateUtils.completeTime("2023-09-12"));
        times.add(DateUtils.completeTime("2023-10-12"));
        times.add(DateUtils.completeTime("2023-11-13"));
        times.add(DateUtils.completeTime("2023-12-12"));
        times.add(DateUtils.completeTime("2024-01-12"));
        times.add(DateUtils.completeTime("2024-02-19"));
        times.add(DateUtils.completeTime("2024-03-12"));
        times.add(DateUtils.completeTime("2024-04-12"));

        List<KLineData> datas = dataService.queryKLineData("000300.SH", 240, 0, times);

        List<Operation> operationList = new ArrayList<>();
        for (KLineData data : datas) {
            Operation operation = new Operation();
            operation.price = data.getClose();
            operation.principal = 1000;
            operation.date = data.getTime();
            operationList.add(operation);
        }

        List<YieldData> yieldDataList = calculatorV1.calculateEachInvestment(operationList);
        Collections.reverse(yieldDataList);

        Assert.assertEquals(operationList.size(), yieldDataList.size());
        YieldData data = yieldDataList.get(0);
        Assert.assertEquals("2024-04-12", data.date);
        Assert.assertEquals(12000d, data.totalCost, 0d);
        Assert.assertEquals(11486, data.finalAmount, 1d);
        Assert.assertEquals(-514, data.profit, 1d);
        Assert.assertEquals(-0.042f, data.returnRate, 0.001f);
        Assert.assertEquals(3.3d, data.quota, 0.11d);
    }

    @Test
    public void testCalculateDaily() {
        LocalDataService dataService = new LocalDataService();
        InvestmentCalculatorV1 calculatorV1 = new InvestmentCalculatorV1();

        String startTime = DateUtils.completeTime("2023-05-12");
        String endTime = DateUtils.completeTime("2024-05-10");
        List<KLineData> datas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

        String dates = "" +
                "2024-04-12" +
                "2024-03-12" +
                "2024-02-19" +
                "2024-01-12" +
                "2023-12-12" +
                "2023-11-13" +
                "2023-10-12" +
                "2023-09-12" +
                "2023-08-14" +
                "2023-07-12" +
                "2023-06-12" +
                "2023-05-12";
        List<Operation> operationList = new ArrayList<>();
        for (int i = 0; i < datas.size(); i += 1) {
            KLineData data = datas.get(i);
            if (!dates.contains(data.getTime())) {
                continue;
            }

            Operation operation = new Operation();
            operation.price = data.getClose();
            operation.principal = 1000;
            operation.date = data.getTime();
            operationList.add(operation);
        }

        List<YieldData> yieldDataList = calculatorV1.calculateDaily(datas, operationList);
        Collections.reverse(yieldDataList);
        Assert.assertEquals(241, yieldDataList.size());

        //最小盈利
        YieldData mostMinProfit = YieldDataUtils.findMinProfit(yieldDataList);
        //最小盈利率
        YieldData mostMinYield = YieldDataUtils.findMinProfitPercentage(yieldDataList);
        //最大盈利
        YieldData mostMaxProfit = YieldDataUtils.findMaxProfit(yieldDataList);

        System.out.println("最小盈利：" + new Gson().toJson(mostMinProfit));
        System.out.println("最小盈利率：" + new Gson().toJson(mostMinYield));
        System.out.println("最大盈利：" + new Gson().toJson(mostMaxProfit));

        Assert.assertEquals("2024-02-02", mostMinProfit.date);
        Assert.assertEquals(9000, mostMinProfit.totalCost, 0d);
        Assert.assertEquals(7775, mostMinProfit.finalAmount, 1d);
        Assert.assertEquals(-1225, mostMinProfit.profit, 1d);
        Assert.assertEquals(-0.136f, mostMinProfit.returnRate, 0.001f);
        Assert.assertEquals(2.445d, mostMinProfit.quota, 0.001d);

        Assert.assertEquals("2024-05-10", mostMaxProfit.date);
        Assert.assertEquals(12000, mostMaxProfit.totalCost, 0d);
        Assert.assertEquals(12117, mostMaxProfit.finalAmount, 1d);
        Assert.assertEquals(117, mostMaxProfit.profit, 1d);
        Assert.assertEquals(0.009f, mostMaxProfit.returnRate, 0.001f);
        Assert.assertEquals(3.305d, mostMaxProfit.quota, 0.001d);
    }
}
