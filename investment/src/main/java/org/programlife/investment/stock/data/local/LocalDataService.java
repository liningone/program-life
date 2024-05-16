package org.programlife.investment.stock.data.local;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.programlife.investment.stock.util.DateUtils;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.KLineDataUtils;
import org.programlife.investment.stock.data.StockDataService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import java.util.*;

public class LocalDataService implements StockDataService {

    @Override
    public List<KLineData> queryKLineData(String stockSymbol, int scale, int ma, String startTime, String endTime) {
        List<String> dataFiles = new ArrayList<>();
        int startYear = DateUtils.getYear(startTime);
        int endYear = DateUtils.getYear(endTime);
        for (int i = startYear; i <= endYear; i ++) {
            //stock.399300.SZ.2024.daily.price.json
            String fileName = String.format("stock/%s/%s_daily_price.json", stockSymbol, i);
            //System.out.println(fileName);
            dataFiles.add(fileName);
        }

        List<KLineData> result = new ArrayList();

        //加载json文件
        Gson gson = new Gson();
        for (String fileName : dataFiles) {
            String jsonStr = getFileContent(fileName);
            List<KLineData> list = gson.fromJson(jsonStr, new TypeToken<List<KLineData>>(){}.getType());
            result.addAll(list);
        }

        //过滤数据
        result = KLineDataUtils.filter(result, startTime, endTime);

        return result;
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

        /*
        //注意getResource("")里面是空字符串
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String filePath = path + fileName;
        System.out.println(filePath);

        byte[] bytes;
        try {
            filePath = "D:/workspace/program-life/investment/target/classes/stock/399300.SZ/2024.daily.closing.price.json";
            System.out.println(filePath);
            bytes = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String content2 = new String(bytes, StandardCharsets.UTF_8);
        return content2;
         */
    }

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

    public void saveData(String stockSymbol, String year, String type, List<KLineData> list) {
        Collections.sort(list, new Comparator<KLineData>() {
            @Override
            public int compare(KLineData o1, KLineData o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });

        String fileName = String.format("stock/%s/%s_%s.json", stockSymbol, year, type);
        URL url = this.getClass().getResource("/");
        String fileFullName = url.getPath().replace("target/classes", "src/main/resources");
        fileFullName += fileName;

        List<String> lines = new ArrayList();
        Gson gson = new Gson();
        String listToJsonString = gson.toJson(list);
        lines.add(listToJsonString);
        try {
            FileUtils.writeLines(new File(fileFullName), "UTF-8", lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
