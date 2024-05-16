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
        result.holdingYield = (float) (result.holdingProfit / result.totalCost);
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
            income.holdingYield = (float) (income.holdingProfit / income.totalCost);
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

}

/*
class Summary extends IncomeData {

}

class IncomeStatement {
    public Summary summary;
    public List<IncomeData> history;
}
 */

