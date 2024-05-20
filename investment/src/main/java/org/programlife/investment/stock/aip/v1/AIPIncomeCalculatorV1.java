package org.programlife.investment.stock.aip.v1;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.aip.AIPIncomeCalculator;
import org.programlife.investment.stock.aip.AIPIncomeStatement;
import org.programlife.investment.stock.aip.AIPOptions;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.KLineDataUtils;
import org.programlife.investment.stock.data.StockDataService;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


/*

依赖：
    股票数据

生成定投操作列表
    计算下一次交易时间
        天
            last day + 1
            如果非交易日+1
        周
            每个周的第一个天[yyyy年-m月-d日，]
                [目标星期day， 下一个交易日（可能会跨周）]
        月
            每个月的第一天[yyyy年-n月-1日， ]

        非交易日则顺延到下一个交易日买入
 */
public class AIPIncomeCalculatorV1 implements AIPIncomeCalculator {
    private StockDataService dataService;

    private List<KLineData> stockDatas;

    @Override
    public AIPIncomeStatement calculate(AIPOptions options) {
        this.dataService = new LocalDataService();
        AIPOptionsV1 optionsV1 = (AIPOptionsV1) options;
        String startTime = optionsV1.getStartTime();
        String endTime = optionsV1.getEndTime();
        this.stockDatas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

        AIPCalendar calendar = new WeekCalendar(4);
        List<String> dates = calendar.generate(startTime, endTime);
        for (String date : dates) {
            System.out.println(getRecentTradingDay(date));
        }

        return null;
    }

    /*
    每个市场的交易日并不相同
    可以从k线图数据中判断是否是交易日
     */
    private boolean isTradeDay(String time) {
        return KLineDataUtils.search(stockDatas, time) != null;
    }

    /*
    如果当天是非交易日，顺延到下个交易日
    例如 2024-05-02 -> 2024-05-06
     */
    private String getRecentTradingDay(String date) {
        boolean isTradeDay = isTradeDay(date);
        if(isTradeDay) {
            return date;
        }

        int idx = KLineDataUtils.searchNearestIndex(stockDatas, date, true);
        return stockDatas.get(idx).getTime();
    }

    @Test
    public void testGetRecentTradingDay() {
        String date = "2024-05-02";
        StockDataService dataService = new LocalDataService();
        String startTime = DateUtils.completeTime("2024-04-30");
        String endTime = DateUtils.completeTime("2024-05-10");
        this.stockDatas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

        Assert.assertEquals("2024-05-06", getRecentTradingDay(date));
    }

    @Test
    public void testWeekly() {
        /*
        生成每个周1号的时间
        2024-04-01
        2024-04-08
         */
        StockDataService dataService = new LocalDataService();
        String startTime = DateUtils.completeTime("2024-04-10");
        String endTime = DateUtils.completeTime("2024-05-10");
        this.stockDatas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

        AIPCalendar calendar = new WeekCalendar(4);
        List<String> dates = calendar.generate(startTime, endTime);
        for (String date : dates) {
            System.out.println(getRecentTradingDay(date));
        }
    }

    @Test
    public void testMonthly() {
        /*
        生成每个月1号的时间
        2024-01-01
        2024-02-01
        2024-03-01
         */
        StockDataService dataService = new LocalDataService();
        //String startTime = DateUtils.completeTime("2023-05-12");
        String startTime = DateUtils.completeTime("2023-04-10");
        String endTime = DateUtils.completeTime("2024-05-10");
        this.stockDatas = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

    }
}



