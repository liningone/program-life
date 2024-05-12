package org.programlife.investment.stock.data;

import java.util.List;

public interface StockDataService {
    /*
    查询指定时间范围的股票数据

    stockSymbol=[股票代码]
    scale=[时间周期]
    ma=[均线周期]
     */
    List<KLineData> queryKLineData(String stockSymbol, int scale, int ma, long startTimeMills, long endTimeMills);

    //TODO
    /*
    查询指定时间的股票数据
     */
    //List<KLineData> queryKLineData(String stockSymbol, int scale, int ma, List<String> dates);
}
