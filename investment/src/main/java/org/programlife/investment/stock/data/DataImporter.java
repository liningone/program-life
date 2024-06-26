package org.programlife.investment.stock.data;

import org.apache.commons.io.FileUtils;
import org.programlife.investment.stock.data.internet.alphavantage.AlphaVantageDataService;
import org.programlife.investment.stock.data.internet.sina.SinaDataService;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.util.DateUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataImporter {
    private LocalDataService localDataService = new LocalDataService();

    private AlphaVantageDataService alphaVantageDataService = new AlphaVantageDataService();
    private SinaDataService sinaDataService = new SinaDataService();

    public void alphaVantageToLocal(String stockSymbol, String type, String startTime) {
        List<KLineData> list = alphaVantageDataService.queryKLineData(stockSymbol, 240, 0, startTime, DateUtils.getCurrentTime());
        Map<String, List<KLineData>> map = new HashMap<>();
        for (KLineData data : list) {
            String year = data.getTime().substring(0, 4);
            List<KLineData> subList = map.computeIfAbsent(year, k -> new ArrayList<>());
            subList.add(data);
        }

        for (String year : map.keySet()) {
            List<KLineData> subList = map.get(year);
            localDataService.saveData(stockSymbol, year, type, subList);
        }
    }

    public void sinaToLocal(String stockSymbol, String type, String startTime) {
        List<KLineData> list = sinaDataService.queryKLineData(stockSymbol, 240, -1,
                startTime, DateUtils.getCurrentTime());
        Map<String, List<KLineData>> map = new HashMap<>();
        for (KLineData data : list) {
            String year = data.getTime().substring(0, 4);
            List<KLineData> subList = map.computeIfAbsent(year, k -> new ArrayList<>());
            subList.add(data);
        }

        for (String year : map.keySet()) {
            List<KLineData> subList = map.get(year);
            localDataService.saveData(stockSymbol, year, type, subList);
        }
    }

    private String getFileContent(String fileName) {
        String content = null;
        URL url = this.getClass().getResource("/" + fileName);
        try {
            content = FileUtils.readFileToString(new File(url.getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
    }

    public static void main(String[] args) {
        DataImporter importer = new DataImporter();

        //importer.alphaVantageToLocal("SPY", "daily_price", "2000-01-01 00:00:00");

        importer.sinaToLocal("000001.SH", "daily_price", "2010-01-01 00:00:00");
        importer.sinaToLocal("000300.SH", "daily_price", "2010-01-01 00:00:00");
        importer.sinaToLocal("399989.SZ", "daily_price", "2010-01-01 00:00:00");
        importer.sinaToLocal("000932.SH", "daily_price", "2010-01-01 00:00:00");
    }
}
