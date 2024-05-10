package org.programlife.investment.stock.data.local;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.programlife.investment.stock.data.KLineData;

import java.util.ArrayList;
import java.util.List;

public class JSONExample {
   private static void testJSONStrToList() {
       String str = "[{\"time\":\"2019-12-04\",\"value\":4},{\"time\":\"2019-12-05\",\"value\":4}]";
       Gson gson = new Gson();
       List<KLineData> list = gson.fromJson(str, new TypeToken<List<KLineData>>(){}.getType());
   }

   private static void listToJSON() {
       List<KLineData> subList = new ArrayList<>();
       Gson gson = new Gson();
       String listToJsonString = gson.toJson(subList);
   }

   public static void main(String[] args) {
       testJSONStrToList();
   }
}