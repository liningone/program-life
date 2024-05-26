package org.programlife.investment.stock.data.internet.sina;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.programlife.investment.stock.data.KLineDataUtils;
import org.programlife.investment.stock.util.DateUtils;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.StockDataService;

import java.util.List;

/*
新浪财经API
    获取各个时间段行情图
        http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=[股票代码]&scale=[时间周期]&ma=[均线周期]&datalen=[数据长度]
        参数
            symbol=[股票代码]
            scale=[时间周期]    查询日线scale=240
            ma=[均线周期]       查询日线ma=no
            datalen=[数据长度]
        返回结果：获取5、10、30、60、240分钟JSON数据；day日期、open开盘价、high最高价、low最低价、close收盘价、volume成交量；向前复权的数据。
        示例：
            要获取股票代码为"sh600519"（贵州茅台）的日线K线图数据
            http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=sh600519&scale=240&ma=no&datalen=1023

 */
public class SinaDataService implements StockDataService {
    private static final String BASE_URL = "https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData";

    @Override
    public List<KLineData> queryKLineData(String stockSymbol, int scale, int ma, String startTime, String endTime) {
        stockSymbol = getSinaStockSymbol(stockSymbol);

        String maStr = "";
        if (ma > 0) {
            maStr = Integer.toString(ma);
        } else {
            maStr = "no";
        }

        int dataLen = 0;
        int startYear = DateUtils.getYear(startTime);
        int endYear = DateUtils.getYear(endTime);
        for (int i = startYear; i <= endYear; i ++) {
            if (scale == 240) {// 日线
                dataLen += 260;//一年最多有260个交易日
            }
            //TODO 更精确的计算dataLen
        }

        String url = String.format("%s?symbol=%s&scale=%s&ma=%s&datalen=%s",
                BASE_URL, stockSymbol, scale, maStr, dataLen);

        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        System.out.println(url);
        String jsonResponse = null;
        try {
            HttpResponse response = client.execute(request);
            jsonResponse = EntityUtils.toString(response.getEntity());
            //System.out.println("Response: " + jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<KLineData> result = SinaOutputFormat.convertToSeries(jsonResponse);
        result = KLineDataUtils.filter(result, startTime, endTime);
        return result;
    }

    /*
    000001.SH -> sh000001
     */
    private String getSinaStockSymbol(String symbol) {
        if (symbol.endsWith(".SH")) {
            return String.format("sh%s", symbol.substring(0, symbol.length() - 3));
        }
        if (symbol.endsWith(".SZ")) {
            return String.format("sz%s", symbol.substring(0, symbol.length() - 3));
        }
        return symbol;
    }

    @Override
    public List<KLineData> queryKLineData(String stockSymbol, int scale, int ma, List<String> specifiedTimes) {
        String startTime = specifiedTimes.get(0);
        String endTime = specifiedTimes.get(specifiedTimes.size() - 1);
        List<KLineData> res = queryKLineData(stockSymbol, scale, ma, startTime, endTime);
        res = KLineDataUtils.filter(res, specifiedTimes);
        if (res.size() != specifiedTimes.size()) {
            throw new RuntimeException("queryKLineData error");
        }
        return res;
    }
}
