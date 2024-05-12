package org.programlife.investment.stock.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.programlife.investment.stock.data.KLineData;

import java.util.ArrayList;
import java.util.List;

public class JSONExample {
    @Test
    public void testJSONStrToList() {
       String str = "[{\"time\":\"2019-12-04\",\"value\":4},{\"time\":\"2019-12-05\",\"value\":4}]";
       Gson gson = new Gson();
       List<KLineData> list = gson.fromJson(str, new TypeToken<List<KLineData>>(){}.getType());
    }

    @Test
    public void listToJSON() {
        List<KLineData> subList = new ArrayList<>();
        Gson gson = new Gson();
        String listToJsonString = gson.toJson(subList);
    }

    @Test
    public void testObj2JsonStr() {
        KLineData data = new KLineData();
        data.setTime("2023-02-12");
        System.out.println(new Gson().toJson(data));
    }
}