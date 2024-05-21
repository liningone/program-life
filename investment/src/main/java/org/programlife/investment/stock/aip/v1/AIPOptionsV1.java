package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.aip.AIPOptions;

/*
支持参数：
目标股票
定投起始时间
定投结束时间
单次定投金额
定投周期
定投总次数   (v1不支持)
全部定投金额 （v1不支持）
 */
public class AIPOptionsV1 implements AIPOptions {
    private String stockSymbol;

    private String startTime;

    private String endTime;

    //定投金额
    private float singleAmount;

    /*
    定投周期:
    每日
    每周
    每2周 (v1不支持)
    每月
     */
    private InvestmentPeriodicity periodicity;

    /*
    每周  周1-周五
    每月  1日-28日
     */
    private Integer periodicityParameter;

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public float getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(float singleAmount) {
        this.singleAmount = singleAmount;
    }

    public InvestmentPeriodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(InvestmentPeriodicity periodicity) {
        this.periodicity = periodicity;
    }

    public Integer getPeriodicityParameter() {
        return periodicityParameter;
    }

    public void setPeriodicityParameter(Integer periodicityParameter) {
        this.periodicityParameter = periodicityParameter;
    }
}
