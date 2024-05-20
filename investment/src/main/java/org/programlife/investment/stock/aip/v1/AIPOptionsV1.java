package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.aip.AIPOptions;

public class AIPOptionsV1 implements AIPOptions {
    private String stockSymbol;

    private String startTime;

    private String endTime;

    //定投金额
    private float amount;

    //定投周期
    //每日
    //每1周  周1-周五
    //每2周  周1-周五
    //每月  1日-28日
    private String duration;

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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
