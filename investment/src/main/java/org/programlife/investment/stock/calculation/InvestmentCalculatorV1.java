package org.programlife.investment.stock.calculation;

import com.google.gson.Gson;
import org.junit.Test;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;
import org.programlife.investment.stock.util.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
投资计算器：计算投资收益
    需求：根据操作历史计算收益历史
    输入：
        股票最新信息
            价格，时间
        操作列表
            操作类型(买入，卖出)，时间, 股票价格，金额，手续费|申购费（v1不支持）
    输出：
        收益历史
            每次操作时间的收益情况: 时间，本金，资产，股票价格，收益率，年化收益率
            收益历史（时间单位天）
            最大亏损率
            最大盈利率

    TODO
        支持卖出操作
        支持年化收益率(IRR算法)
        收益历史
        最大亏损/亏损率
            遍历收益历史
        最大盈利/盈利率
            遍历收益历史

 */
public class InvestmentCalculatorV1 implements InvestmentCalculator{

    //计算当前价格的收益情况
    public IncomeData calculate1(KLineData stockPrice, List<Operation> operations) {
        double quota = 0;
        double cost = 0;

        for (Operation operation : operations) {
            cost += operation.principal;
            quota += operation.principal / operation.price;
        }

        IncomeData result = new IncomeData();
        result.date = stockPrice.getTime();
        result.holdingAmount = stockPrice.getClose() * quota;
        result.totalCost = cost;
        result.holdingProfit = result.holdingAmount - result.totalCost;
        //result.holdingYield = result.holdingProfit / result.totalCost;
        result.quota = quota;
        return  result;
    }

    //计算每个操作时间的收益情况
    public List<IncomeData> calculate2(List<Operation> operations) {
        double quota = 0;
        double cost = 0;

        List<IncomeData> history = new ArrayList<>();
        for (Operation operation : operations) {
            cost += operation.principal;
            quota += operation.principal / operation.price;

            IncomeData income = new IncomeData();
            income.date = operation.date;
            income.quota = quota;
            income.holdingAmount = income.quota * operation.price;
            income.totalCost = cost;
            income.holdingProfit = income.holdingAmount - income.totalCost;
            //income.holdingYield = income.holdingProfit / income.totalCost;
            history.add(income);
        }

        return history;
    }

    /*
    计算每天的收益情况（时间单位：天）
    从第一个操作开始计算收益历史
    交易历史不包含非交易日
     */
    public List<IncomeData> calculate3(List<KLineData> stockPrices, List<Operation> operations) {
        double quota = 0;
        double cost = 0;

        List<IncomeData> history = new ArrayList<>();

        int idx = 0;
        for(KLineData data : stockPrices) {
            if(idx < operations.size()) {
                Operation operation = operations.get(idx);
                if (data.getTime().equals(operation.date)) {
                    cost += operation.principal;
                    quota += operation.principal / operation.price;
                    //double temp = MathUtils.doubleDivide(operation.principal, operation.price, 3);
                    idx ++;
                }
            }

            if (quota == 0d) {
                continue;
            }

            IncomeData income = new IncomeData();
            income.date = data.getTime();
            income.quota = quota;

            income.holdingAmount = income.quota * data.getClose();
            //income.holdingAmount = MathUtils.formatDouble(income.holdingAmount, 2);
            income.totalCost = cost;
            income.holdingProfit = income.holdingAmount - income.totalCost;
            income.holdingYield = (float) (income.holdingProfit / income.totalCost) ;
            //income.holdingYield = MathUtils.formatFloat(income.holdingYield, 2);
            history.add(income);
        }

        return history;
    }

    @Test
    public void test() {
        LocalDataService dataService = new LocalDataService();
        InvestmentCalculatorV1 calculatorV1 = new InvestmentCalculatorV1();

        String startTimeMills = "2023-05-12";
        String endTimeMills = "2024-05-10";

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
        List<KLineData> datas = dataService.queryKLineData("000300.SH", 240, 0, startTimeMills, endTimeMills);

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

        IncomeData summary = calculatorV1.calculate1(datas.get(datas.size() - 1), operationList);
        System.out.println(new Gson().toJson(summary) + '\n');

        List<IncomeData> incomeDataList = calculatorV1.calculate2(operationList);
        Collections.reverse(incomeDataList);
        for (IncomeData data : incomeDataList) {
            System.out.println(new Gson().toJson(data));
        }
    }

    @Test
    public void test2() {
        LocalDataService dataService = new LocalDataService();
        InvestmentCalculatorV1 calculatorV1 = new InvestmentCalculatorV1();

        String startTime = "2023-05-12 00:00:00";
        String endTime = "2024-05-10 00:00:00";

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
        List<KLineData> datas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

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

        for (IncomeData data : incomeDataList) {
            //System.out.println(new Gson().toJson(data));
        }
    }
    //增量计算
    /*
    public IncomeStatement recalculate(IncomeStatement origin, KLineData current, List<Operation> operationList)
     */
}


class Operation {
    public String date;
    public int type = 1;  //买入/卖出
    public double price;
    public double principal; //本金
}

class IncomeData {
    public String date;
    public double totalCost; //本金
    public double holdingAmount; //持有金额
    public double holdingProfit; //持有收益
    public float holdingYield; //持有收益率
    public double quota; //持有份额
}

class Summary extends IncomeData {

}

class IncomeStatement {
    public Summary summary;
    public List<IncomeData> history;
}
