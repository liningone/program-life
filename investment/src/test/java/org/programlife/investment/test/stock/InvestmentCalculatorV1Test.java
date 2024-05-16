package org.programlife.investment.test.stock;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.calculation.IncomeData;
import org.programlife.investment.stock.calculation.InvestmentCalculatorV1;
import org.programlife.investment.stock.calculation.Operation;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvestmentCalculatorV1Test {
    @Test
    public void test1() {
        LocalDataService dataService = new LocalDataService();
        InvestmentCalculatorV1 calculatorV1 = new InvestmentCalculatorV1();

        String dates = "" +
                "2024-04-12\t1,000.00\t3475.84\t12,000.00\t11,486.78\t-7.85%\t-4.28%\n" +
                "2024-03-12\t1,000.00\t3597.49\t11,000.00\t10,853.80\t-2.65%\t-1.33%\n" +
                "2024-02-19\t1,000.00\t3403.81\t10,000.00\t9,323.30\t-14.48%\t-6.77%\n" +
                "2024-01-12\t1,000.00\t3284.17\t9,000.00\t8,030.75\t-24.75%\t-10.77%\n" +
                "2023-12-12\t1,000.00\t3426.8\t8,000.00\t7,336.09\t-21.21%\t-8.30%\n" +
                "2023-11-13\t1,000.00\t3579.41\t7,000.00\t6,618.26\t-15.79%\t-5.45%\n" +
                "2023-10-12\t1,000.00\t3702.38\t6,000.00\t5,811.27\t-10.51%\t-3.15%\n" +
                "2023-09-12\t1,000.00\t3760.6\t5,000.00\t4,886.93\t-8.83%\t-2.26%\n" +
                "2023-08-14\t1,000.00\t3855.91\t4,000.00\t3,985.44\t-1.74%\t-0.36%\n" +
                "2023-07-12\t1,000.00\t3843.44\t3,000.00\t2,975.79\t-4.77%\t-0.81%\n" +
                "2023-06-12\t1,000.00\t3844.43\t2,000.00\t1,976.30\t-9.17%\t-1.19%\n" +
                "2023-05-12";

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

        times.clear();
        times.add(DateUtils.completeTime("2024-05-10"));
        datas = dataService.queryKLineData("000300.SH", 240, 0, times);

        IncomeData summary = calculatorV1.calculate1(datas.get(0), operationList);
        //System.out.println(new Gson().toJson(summary));
        Assert.assertEquals("2024-05-10", summary.date);
        Assert.assertEquals(12000d, summary.totalCost, 0d);
        Assert.assertEquals(12117, summary.holdingAmount, 1d);
        Assert.assertEquals(117, summary.holdingProfit, 1d);
        Assert.assertEquals(0.009f, summary.holdingYield, 0.001f);
        Assert.assertEquals(3.3d, summary.quota, 0.11d);
    }

    @Test
    public void test2() {
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

        List<IncomeData> incomeDataList = calculatorV1.calculate2(operationList);
        Collections.reverse(incomeDataList);

        Assert.assertEquals(operationList.size(), incomeDataList.size());
        IncomeData data = incomeDataList.get(0);
        Assert.assertEquals("2024-04-12", data.date);
        Assert.assertEquals(12000d, data.totalCost, 0d);
        Assert.assertEquals(11485, data.holdingAmount, 1d);
        Assert.assertEquals(-514, data.holdingProfit, 1d);
        Assert.assertEquals(-0.042f, data.holdingYield, 0.001f);
        Assert.assertEquals(3.3d, data.quota, 0.11d);
    }

    @Test
    public void test3() {
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

        List<IncomeData> incomeDataList = calculatorV1.calculate3(datas, operationList);
        Collections.reverse(incomeDataList);
        Assert.assertEquals(241, incomeDataList.size());

        //亏最多钱
        IncomeData mostMinProfit = incomeDataList.get(0);
        //最大亏损率
        IncomeData mostMinYield = incomeDataList.get(0);
        //盈利最多
        IncomeData mostMaxProfit = incomeDataList.get(0);

        for (IncomeData data : incomeDataList) {
            if (data.holdingProfit > mostMaxProfit.holdingProfit) {
                mostMaxProfit = data;
            }
            if(data.holdingYield < mostMinYield.holdingYield) {
                mostMinYield = data;
            }
            if (data.holdingProfit < mostMinProfit.holdingProfit) {
                mostMinProfit = data;
            }
        }

        System.out.println("亏损最多钱：" + new Gson().toJson(mostMinProfit));
        System.out.println("最大亏损率：" + new Gson().toJson(mostMinYield));
        System.out.println("盈利最多钱：" + new Gson().toJson(mostMaxProfit));

        Assert.assertEquals("2024-02-02", mostMinProfit.date);
        Assert.assertEquals(9000, mostMinProfit.totalCost, 0d);
        Assert.assertEquals(7774, mostMinProfit.holdingAmount, 1d);
        Assert.assertEquals(-1225, mostMinProfit.holdingProfit, 1d);
        Assert.assertEquals(-0.136f, mostMinProfit.holdingYield, 0.001f);
        Assert.assertEquals(2.445d, mostMinProfit.quota, 0.001d);

        Assert.assertEquals("2024-05-10", mostMaxProfit.date);
        Assert.assertEquals(12000, mostMaxProfit.totalCost, 0d);
        Assert.assertEquals(12117, mostMaxProfit.holdingAmount, 1d);
        Assert.assertEquals(117, mostMaxProfit.holdingProfit, 1d);
        Assert.assertEquals(0.009f, mostMaxProfit.holdingYield, 0.001f);
        Assert.assertEquals(3.305d, mostMaxProfit.quota, 0.001d);
    }
}
