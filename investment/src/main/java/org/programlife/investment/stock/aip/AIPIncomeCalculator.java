package org.programlife.investment.stock.aip;

/*
AIP : 定投，全称为Automatic Investment Plan

定投收益计算器：使用股票历史价格去计算定投计划的收益情况
输入：定投参数
输出：定投收益情况
依赖：股票历史价格
其他因素：人为干预定投 (v1不支持)
 */
public interface AIPIncomeCalculator {

    public AIPIncomeStatement calculate(AIPOptions options);

    //void beforeEachPurchase();

}



