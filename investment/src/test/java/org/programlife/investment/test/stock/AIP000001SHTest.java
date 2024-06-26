package org.programlife.investment.test.stock;

import org.junit.Test;
import org.programlife.investment.stock.aip.AIPYieldStatement;
import org.programlife.investment.stock.aip.InvestmentPeriodicity;
import org.programlife.investment.stock.aip.calendar.MonthCalendar;
import org.programlife.investment.stock.aip.v1.AIPOptionsV1;
import org.programlife.investment.stock.aip.v1.AIPUtils;
import org.programlife.investment.stock.aip.v1.AIPYieldStatementV1;
import org.programlife.investment.stock.calculation.InvestmentCalculatorV1;
import org.programlife.investment.stock.calculation.Operation;
import org.programlife.investment.stock.calculation.YieldData;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.StockDataService;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.programlife.investment.stock.util.LineChartUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AIP000001SHTest {

    //定投间隔1个月，时长3-24个月，滚动的
    @Test
    public void test() {
        Map<Integer, int[]> map = new HashMap<>();

        AIPOptionsV1 template = new AIPOptionsV1();
        template.setPeriodicity(InvestmentPeriodicity.MONTHLY);
        template.setPeriodicityParameter(1);

        for (int monthNum = 3; monthNum <= 12; monthNum ++) {
            List<AIPYieldStatement> yieldDataList = AIPUtils.batchCalculate("000001.SH",
                    "2010-01-01", "2024-01-01", 12000, monthNum, template);

            List<Double> list = new ArrayList<>();
            for(AIPYieldStatement data : yieldDataList) {
                list.add(((AIPYieldStatementV1) data).currentYield.profit);
            }

            Percentile percentile = new Percentile();
            double[] array = list.stream().mapToDouble(Double::doubleValue).toArray();

            int num = 100;
            int[] percentileArr = new int[num/5 + 1];
            for (int per = 0; per <= num; per += 5) {
                int idx = per;
                if (per == 0) {
                    percentileArr[idx] = (int) percentile.evaluate(array, 0.01);
                } else {
                    percentileArr[idx/5] = (int) percentile.evaluate(array, per);
                }
            }
            map.put(monthNum, percentileArr);
        }

        LineChartUtils.saveChartAsPNG(map);
    }

    @Test
    public void test2() {
        StockDataService dataService = new LocalDataService();
        InvestmentCalculatorV1 calculatorV1 = new InvestmentCalculatorV1();

        MonthCalendar monthCalendar = new MonthCalendar(1);
        List<String> dates = monthCalendar.getExpectDayTime("2010-01-01", "2023-01-01");

        List<Double> list = new ArrayList<>();

        for(String dateStr : dates) {
            String startTime = DateUtils.completeTime(dateStr);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            LocalDate oneYearLater = date.plusYears(1).minusDays(1);
            String endTime = DateUtils.completeTime(oneYearLater.format(formatter));

            List<KLineData> datas = dataService.queryKLineData("000001.SH", 240, 0,
                    startTime, endTime);

            List<Operation> operationList = new ArrayList<>();
            Operation operation = new Operation();
            operation.price = datas.get(0).getClose();
            operation.principal = 12000;
            operation.date = datas.get(0).getTime();
            operationList.add(operation);

            YieldData summary = calculatorV1.calculate(datas.get(datas.size() - 1), operationList);
            list.add(summary.profit);

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
