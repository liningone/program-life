package org.programlife.investment.stock.calculation;

import org.programlife.investment.stock.data.KLineData;

import java.util.ArrayList;
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
        最大亏损/亏损率
            遍历收益历史
        最大盈利/盈利率
            遍历收益历史
        支持卖出操作
 */
public class InvestmentCalculatorV1 implements InvestmentCalculator{

    //计算当前价格的收益情况
    public YieldData calculate(KLineData stockPrice, List<Operation> operations) {
        double quota = 0;
        double cost = 0;

        double[] cashFlows = new double[operations.size() + 1];
        String[] dates = new String[operations.size() + 1];
        int idx = 0;

        for (Operation operation : operations) {
            cost += operation.principal;
            quota += operation.principal / operation.price;

            cashFlows[idx] = -operation.principal;
            dates[idx ++] = operation.date;
        }

        YieldData result = new YieldData();
        result.date = stockPrice.getTime();
        result.finalAmount = stockPrice.getClose() * quota;
        result.totalCost = cost;
        result.profit = result.finalAmount - result.totalCost;
        result.returnRate = (float) (result.profit / result.totalCost);
        result.quota = quota;

        cashFlows[idx] = result.finalAmount;
        dates[idx] = stockPrice.getTime();
        result.annualIRR = IRRCalculator.calculateAnnualIRR(cashFlows, dates);

        return  result;
    }


    //计算每个操作时间的收益情况
    public List<YieldData> calculateEachInvestment(List<Operation> operations) {
        double quota = 0;
        double cost = 0;

        List<YieldData> history = new ArrayList<>();
        for (Operation operation : operations) {
            cost += operation.principal;
            quota += operation.principal / operation.price;

            YieldData income = new YieldData();
            income.date = operation.date;
            income.quota = quota;
            income.finalAmount = income.quota * operation.price;
            income.totalCost = cost;
            income.profit = income.finalAmount - income.totalCost;
            income.returnRate = (float) (income.profit / income.totalCost);
            history.add(income);
        }

        return history;
    }

    /*
    计算每天的收益情况（时间单位：天）
    从第一个操作开始计算收益历史
    交易历史不包含非交易日
     */
    public List<YieldData> calculateDaily(List<KLineData> stockPrices, List<Operation> operations) {
        double quota = 0;
        double cost = 0;

        List<YieldData> history = new ArrayList<>();

        int idx = 0;
        for(KLineData data : stockPrices) {
            if(idx < operations.size()) {
                Operation operation = operations.get(idx);
                if (data.getTime().equals(operation.date)) {
                    cost += operation.principal;
                    quota += operation.principal / operation.price;
                    idx ++;
                }
            }

            if (quota == 0d) {
                continue;
            }

            YieldData income = new YieldData();
            income.date = data.getTime();
            income.quota = quota;

            income.finalAmount = income.quota * data.getClose();
            income.totalCost = cost;
            income.profit = income.finalAmount - income.totalCost;
            income.returnRate = (float) (income.profit / income.totalCost) ;
            history.add(income);
        }

        return history;
    }

    public double calculateIRR(KLineData stockPrice, List<Operation> operations) {
        double[] cashFlows = new double[operations.size() + 1];
        String[] dates = new String[operations.size() + 1];
        int idx = 0;

        for (Operation operation : operations) {
            cashFlows[idx] = -operation.principal;
            dates[idx ++] = operation.date;
        }

        //YieldData result = calculate(stockPrice, operations);
        //cashFlows[idx] = result.holdingAmount;
        dates[idx] = stockPrice.getTime();
        return IRRCalculator.calculateAnnualIRR(cashFlows, dates);
    }
}
