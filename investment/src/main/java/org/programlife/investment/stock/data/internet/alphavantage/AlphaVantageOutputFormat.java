package org.programlife.investment.stock.data.internet.alphavantage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.programlife.investment.stock.data.KLineData;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AlphaVantageOutputFormat {
    public static List<KLineData> convertToSeries(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONObject timeSeries = json.getJSONObject("Time Series (Daily)");

        List<KLineData> result = new ArrayList();
        for(String day : timeSeries.keySet()) {
            JSONObject series = timeSeries.getJSONObject(day);
            String openPriceStr = series.getString("1. open");
            String highPriceStr = series.getString("2. high");
            String lowPriceStr = series.getString("3. low");
            String closePriceStr = series.getString("4. close");
            String volumeStr = series.getString("5. volume");
            KLineData dataObj = new KLineData();
            dataObj.setTime(day);
            dataObj.setOpen(doubleStr2Float(openPriceStr));
            dataObj.setHigh(doubleStr2Float(highPriceStr));
            dataObj.setLow(doubleStr2Float(lowPriceStr));
            dataObj.setClose(doubleStr2Float(closePriceStr));
            dataObj.setVolume(Long.parseLong(volumeStr));
            result.add(dataObj);
        }

        return result;
    }

    private static float doubleStr2Float(String doubleStr) {
        return Float.parseFloat(doubleStr);
    }

    public static void main(String[] args) {
        String jsonResponse = "{\n" +
                "    \"Meta Data\": {\n" +
                "        \"1. Information\": \"Daily Prices (open, high, low, close) and Volumes\",\n" +
                "        \"2. Symbol\": \"002340.SZ\",\n" +
                "        \"3. Last Refreshed\": \"2019-12-05\",\n" +
                "        \"4. Output Size\": \"Compact\",\n" +
                "        \"5. Time Zone\": \"US/Eastern\"\n" +
                "    },\n" +
                "    \"Time Series (Daily)\": {\n" +
                "        \"2019-12-05\": {\n" +
                "            \"1. open\": \"4.1300\",\n" +
                "            \"2. high\": \"4.1700\",\n" +
                "            \"3. low\": \"4.1100\",\n" +
                "            \"4. close\": \"4.1600\",\n" +
                "            \"5. volume\": \"13717150\"\n" +
                "        },\n" +
                "        \"2019-12-04\": {\n" +
                "            \"1. open\": \"4.0200\",\n" +
                "            \"2. high\": \"4.1300\",\n" +
                "            \"3. low\": \"4.0200\",\n" +
                "            \"4. close\": \"4.1100\",\n" +
                "            \"5. volume\": \"45928009\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        List<KLineData> res = new AlphaVantageOutputFormat().convertToSeries(jsonResponse);
        String str = new JSONArray(res).toString();
        System.out.println(str);


        Gson gson = new Gson();
        List<KLineData> list = gson.fromJson(str, new TypeToken<List<KLineData>>(){}.getType());
        //System.out.println(list);
    }

}
