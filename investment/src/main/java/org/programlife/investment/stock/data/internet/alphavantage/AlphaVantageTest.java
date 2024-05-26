package org.programlife.investment.stock.data.internet.alphavantage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlphaVantageTest {
    public static void main(String[] args) {
        // 设置AlphaVantage API密钥
        String apiKey = "5DWSG04BZL9NL99L";

        String symbol = "SPY";

        // 构建API请求URL
        String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize=compact&symbol=" + symbol + "&apikey=" + apiKey;

        // 发送HTTP请求并获取响应
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 打印API响应（JSON格式）
            System.out.println("API响应：");
            System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
