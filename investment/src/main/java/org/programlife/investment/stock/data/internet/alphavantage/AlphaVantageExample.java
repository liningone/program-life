package org.programlife.investment.stock.data.internet.alphavantage;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlphaVantageExample {
    //TTK3UTZ4N2NSWYG9
    //U8CZG7DYO1UHTPJK
    //KWFUMYQUAE6QRB0Z
    private static String API_KEY2 = "KWFUMYQUAE6QRB0Z";  // 将YOUR_API_KEY替换成您的Alpha Vantage API密钥
    private static final String BASE_URL = "https://www.alphavantage.co/query";

    public static void main(String[] args) {
        // 设置API请求参数
        String function = "TIME_SERIES_DAILY";
        String symbol = "399300.SZ"; //沪深300
        //symbol = "000300.SHH"; //沪深300
        //symbol = "000001.SHH"; //上证
        symbol = "^GSPC";

        //symbol = "002340.SZ"; //一直能成功
        String outputsize = "full";  // 'full' 或 'compact', full会返回所有历史数据，compact返回最近100条数据

        // 构建URL
        String url = String.format("%s?function=%s&symbol=%s&outputsize=%s&apikey=%s",
                BASE_URL, function, symbol, outputsize, API_KEY2);
        System.out.println(url);
        // 创建HTTP客户端并执行请求
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        //System.out.println(url);
        try {
            HttpResponse response = client.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println("Response: " + jsonResponse);

            List<String> lines = new ArrayList<>();
            lines.add(jsonResponse);
            try {
                FileUtils.writeLines(new File("D:/test.json"), "UTF-8", lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
