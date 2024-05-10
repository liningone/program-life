package org.programlife.investment.stock.data;

import java.util.List;

public interface StockDataService {
    /*
    stockSymbol=[股票代码]
    scale=[时间周期]
    ma=[均线周期]
     */
    List<KLineData> queryKLineData(String stockSymbol, int scale, int ma, long startTimeMills, long endTimeMills);
}
