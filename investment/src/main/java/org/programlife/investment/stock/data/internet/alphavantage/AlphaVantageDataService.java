package org.programlife.investment.stock.data.internet.alphavantage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.StockDataService;

import java.util.List;

/*
Alpha Vantage
API文档
    https://www.alphavantage.co/documentation/
API限制
    免费的接口查a股指数的数据，查一次后要隔几个小时后才能再次查询。
    若在时间内查询，报错信息就说查询语法错误
    似乎是根据请求IP做了限制，即使换了api_key也有时间限制

获取每天的行情
    TIME_SERIES_DAILY
    参数
        outputsize=[数据大小] 'full' 或 'compact', full会返回所有历史数据，compact返回最近100条数据
 */

public class AlphaVantageDataService implements StockDataService {
    private static String API_KEY = "TTK3UTZ4N2NSWYG9";  // 将API_KEY替换成您的Alpha Vantage API密钥
    private static final String BASE_URL = "https://www.alphavantage.co/query";

    @Override
    public List<KLineData> queryKLineData(String stockSymbol, int scale, int ma, String startTime, String endTime) {
        String function = "TIME_SERIES_DAILY";
        String outputsize = "full";  //
        String url = String.format("%s?function=%s&symbol=%s&outputsize=%s&apikey=%s",
                BASE_URL, function, stockSymbol, outputsize, API_KEY);

        // 创建HTTP客户端并执行请求
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

        return AlphaVantageOutputFormat.convertToSeries(jsonResponse);
    }

    @Override
    public List<KLineData> queryKLineData(String stockSymbol, int scale, int ma, List<String> specifiedTimes) {
        throw new RuntimeException("not supported");
    }
}
