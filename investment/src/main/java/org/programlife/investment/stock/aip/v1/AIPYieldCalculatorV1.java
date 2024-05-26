package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.aip.AIPYieldCalculator;
import org.programlife.investment.stock.aip.AIPYieldStatement;
import org.programlife.investment.stock.aip.AIPOptions;
import org.programlife.investment.stock.aip.InvestmentPeriodicity;
import org.programlife.investment.stock.aip.calendar.AIPCalendar;
import org.programlife.investment.stock.aip.calendar.DayCalendar;
import org.programlife.investment.stock.aip.calendar.MonthCalendar;
import org.programlife.investment.stock.aip.calendar.WeekCalendar;
import org.programlife.investment.stock.calculation.InvestmentCalculatorV1;
import org.programlife.investment.stock.calculation.Operation;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.KLineDataUtils;
import org.programlife.investment.stock.data.StockDataService;
import org.programlife.investment.stock.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/*
主要流程
    生成定投操作列表
    计算定投收益

生成定投操作列表
    计算下一次交易时间
        定投周期：天
            last day + 1
            如果非交易日+1
        定投周期：周
            每个周的第一个天[yyyy年-m月-d日，]
                [目标星期day， 下一个交易日（可能会跨周）]
        定投周期：月
            每个月的第一天[yyyy年-n月-1日， ]
        依赖：股票的k线图数据
 */
public class AIPYieldCalculatorV1 implements AIPYieldCalculator {
    private StockDataService dataService;

    private List<KLineData> stockDatas;

    public AIPYieldCalculatorV1(StockDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public AIPYieldStatement calculate(AIPOptions options) {
        AIPOptionsV1 optionsV1 = (AIPOptionsV1) options;
        String startTime = optionsV1.getStartTime();
        String endTime = optionsV1.getEndTime();
        this.stockDatas = dataService.queryKLineData(optionsV1.getStockSymbol(), 240, 0, startTime, endTime);

        List<Operation> operations = new ArrayList<>();
        List<String> dates = this.getExpectDayTime(startTime, endTime,
                optionsV1.getPeriodicity(), optionsV1.getPeriodicityParameter());
        for (String date : dates) {
            date = getRecentTradingDay(date);
            KLineData kLineData = KLineDataUtils.search(this.stockDatas, date);
            Operation operation = new Operation();
            operation.date = getRecentTradingDay(date);
            operation.principal = optionsV1.getSingleAmount();
            operation.price = kLineData.getClose();
            operations.add(operation);
        }

        InvestmentCalculatorV1 calculator = new InvestmentCalculatorV1();

        AIPYieldStatementV1 res = new AIPYieldStatementV1();
        res.yieldForEachInvestment = calculator.calculateEachInvestment(operations);
        res.currentYield = calculator.calculate(this.stockDatas.get(this.stockDatas.size() - 1), operations);
        return res;
    }

    private List<String> getExpectDayTime(String startTime, String endTime,
                                          InvestmentPeriodicity periodicity, Integer periodicityParameter) {
        startTime = DateUtils.parseDateStr(startTime, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);
        endTime = DateUtils.parseDateStr(endTime, DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);

        AIPCalendar calendar = null;
        if (periodicity == InvestmentPeriodicity.MONTHLY) {
            calendar = new MonthCalendar(periodicityParameter);
        } else if (periodicity == InvestmentPeriodicity.WEEKLY) {
            calendar = new WeekCalendar(periodicityParameter);
        } else {
            calendar = new DayCalendar();
        }

        return calendar.getExpectDayTime(startTime, endTime);
    }

    /*
    每个市场的交易日并不相同
    可以从k线图数据中判断是否是交易日
     */
    private boolean isTradeDay(String time) {
        return KLineDataUtils.search(stockDatas, time) != null;
    }

    /*
    如果目标日期是交易日，直接返回
    如果目标日期是非交易日，顺延到下个交易日
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

}



